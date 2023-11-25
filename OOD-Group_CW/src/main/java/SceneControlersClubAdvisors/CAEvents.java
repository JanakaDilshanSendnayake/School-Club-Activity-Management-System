package SceneControlersClubAdvisors;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.EventDataHandling;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


/**
 *  Controller class for club advisor event fxml file which club advisor is interacting
 */
public class CAEvents implements Initializable {


    public TextField newEventIDFieldUpdate;
    /**
     *  Left Side Naviagtion bar buttons
     */
    @FXML private Button caHomeButton;
    @FXML private Button caClubsButton;
    @FXML private Button caEventsButton;
    @FXML private Button caReportsButton;
    @FXML private Button caAccountButton;

    /**
     * Tab Pane fxml id
     */
    @FXML private TabPane caEventsTabPane;
    @FXML private Tab eventNavigationTab;
    @FXML private Tab createNewEventTab;
    @FXML private AnchorPane caNaviEvents;
    @FXML private AnchorPane caViewEvents;
    @FXML private AnchorPane caUpdateEvents;
    @FXML private AnchorPane caCreateNewEvents;


    private Stage stage;
    private Scene scene;
    private Parent root;

    //To validate the data inputs in Create new event tab

    @FXML
    private TextField newEventIDField;
    @FXML
    private Label newEventIDLabel;
    @FXML
    private TextField newEventNameField;
    @FXML
    private Label newEventNameLabel;
    @FXML
    private ComboBox<String> organizingClubComboBox;
    @FXML
    private Label organizingClubComboBoxLabel;
    @FXML
    private TextArea newEventDescriptionTextArea;
    @FXML
    private Label newEventDescriptionLabel;
    @FXML
    private TextField newEventVenueField;
    @FXML
    private Label newEventVenueFieldLabel;
    @FXML
    private DatePicker newEventDatePicker;
    @FXML
    private Label newEventDatePickerLabel;


    // Regular Expressions for new event creation filling form
    private static final String EVENT_NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String EVENT_DESCRIPTION_REGEX = "^(?s).{1,200}$";
    private static final String EVENT_VENUE_REGEX = "^.+$";


    //To validate the data inputs in navigation event tab
    @FXML
    private TextField newEventNameFieldUpdate;
    @FXML
    private Label newEventNameLabelUpdate;
    @FXML
    private ComboBox<String> organizingClubComboBoxUpdate;
    @FXML
    private Label organizingClubComboBoxLabelUpdate;
    @FXML
    private TextArea newEventDescriptionTextAreaUpdate;
    @FXML
    private Label newEventDescriptionLabelUpdate;
    @FXML
    private TextField newEventVenueFieldUpdate;
    @FXML
    private Label newEventVenueFieldLabelUpdate;
    @FXML
    private DatePicker newEventDatePickerUpdate;
    @FXML
    private Label newEventDatePickerLabelUpdate;

    @FXML
    private ComboBox<String> selectClubComboBox;

    @FXML
    private ComboBox<String> selectEventComboBox;


    // Event Objects
    private ChangeListener<String> newEventNameFieldListener;
    private ChangeListener<String> newEventDescriptionTextAreaListener;
    private ChangeListener<String> newEventVenueFieldListener;

    // this variable is used to check whether if there is an ongoing detail update.
    private boolean updateStatus;

    String newEventID ;
    String newEventName;
    String newEventVenue;
    LocalDate newEventDate;
    String newEventDescriptionText;


    public void initialize(URL url, ResourceBundle resourceBundle) {


        updateStatus = false;

        caEventsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (updateStatus && newTab == createNewEventTab) {
                if(showConfirmationAlert("There's an ongoing update. Do you want to cancel it?")){
                    caEventsTabPane.getSelectionModel().select(newTab);
                }else{
                    caEventsTabPane.getSelectionModel().select(oldTab);
                }

            }
        });


        boolean isEventID = false;
        boolean isEventName = false;
        boolean isEventVenue = false;
        boolean isEventDate = false;
        boolean isEventDescription = false;
        boolean isEventClub = false;



        // initializing the event listener to validate the user input in "newClubNameField" text-field
        newEventNameFieldListener = ((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, EVENT_NAME_REGEX, "Name can only contain letters and Underscores.", 30);
            newEventNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newEventNameLabel);
        });


        // initializing the event listener to validate the user input in "newEventDescriptionTextArea" text-field
        newEventDescriptionTextAreaListener = ((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, EVENT_DESCRIPTION_REGEX, "Invalid", 500);
            newEventDescriptionLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newEventDescriptionLabel);
        });

        // initializing the event listener to validate the user input in "newClubNameField" text-field
        newEventVenueFieldListener = ((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, EVENT_VENUE_REGEX, "This field is mandatory.");
            newEventVenueFieldLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newEventVenueFieldLabel);
        });


        // Update event
        EventDataHandling eventDataHandling = new EventDataHandling();
        ObservableList<String> clubList = eventDataHandling.getClubListFromDatabase();
        selectClubComboBox.setItems(clubList);

        selectClubComboBox.setOnAction(event -> {
            String selectedClub = selectClubComboBox.getValue();
            if (selectedClub != null) {
                System.out.println("Selected Club: " + selectedClub);

                // Use the selectedClub value as needed.
                // For example, pass it to another method:
                ObservableList<String> events = eventDataHandling.getEventsByClubName(selectedClub);
                selectEventComboBox.setItems(events);


            } else {
                System.out.println("No club selected.");
            }
        });

        selectEventComboBox.setOnAction(event -> {
            String selectedEvent = selectEventComboBox.getValue();
            if (selectedEvent != null) {
                System.out.println("Selected Event: " + selectedEvent);

                // Use the selectedClub value as needed.
                // For example, pass it to another method:
                ObservableList<Object> observableList = eventDataHandling.getEventsByEventName(selectedEvent);

                for (Object obj : observableList) {
                    System.out.println(observableList);
                }


                System.out.println("right there " + observableList.get(3));


                int newEventID = (Integer) observableList.get(0);
                String newEventName = (String) observableList.get(1);
                String newEventVenue = (String) observableList.get(2);
                newEventDate = (LocalDate) observableList.get(3);
                String organizingClubComboBox = (String) observableList.get(4);
                String newEventDescriptionText = (String) observableList.get(5);

                System.out.println("right here " + newEventDate);

                newEventIDFieldUpdate.setText(String.valueOf(newEventID));
                newEventNameFieldUpdate.setText(newEventName);
                newEventVenueFieldUpdate.setText(newEventVenue);
                newEventDatePickerUpdate.setValue(newEventDate);
                organizingClubComboBoxUpdate.setValue(organizingClubComboBox);
                newEventDescriptionTextAreaUpdate.setText(newEventDescriptionText);


            } else {
                System.out.println("No club selected.");
            }
        });

//        eventDataHandling.getEventListFromDatabase();

    }

    public void eventUpdaterAction(ActionEvent event) throws SQLException {

        EventDataHandling eventDataHandlingUpdate = new EventDataHandling();
        System.out.println("Here : " + newEventDate);
        eventDataHandlingUpdate.deleteExisitingData(newEventID,newEventName, newEventVenue, newEventDate,organizingClubComboBox, newEventDescriptionText);

    }

    /**
     * Validated the Event listerns on fxml files
     * @param value
     * @param regex
     * @param invalidMessage
     * @param maximumCharacterLim
     * @return
     */

    private String validateTextField(String value, String regex, String invalidMessage,int maximumCharacterLim) {
        if (value.matches(regex)) {

            return "Valid";
        } else {
            if (value.length()>maximumCharacterLim){
                return "Maximum character limit exceeded";
            }else{
                return invalidMessage;}
        }
    }


    //Method to handle the color changes of the messages that's shown the text labels
    private void setLabelStyle(String validationMessage, Label label) {
        if (validationMessage.equals("Valid")) {
            label.setStyle("-fx-text-fill: green;");
        } else {
            label.setStyle("-fx-text-fill: red;");
        }
    }

    private String validateTextField(String value, String regex, String invalidMessage) {
        if (value.matches(regex)) {
            return "Valid";
        } else {
            if (value.length()>30){
                return "Maximum character limit exceeded";
            }else{
                return invalidMessage;}
        }
    }

    private void showInfoAlert( String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean showConfirmationAlert(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    public void clickSidebar(ActionEvent actionEvent) throws IOException {

        if(actionEvent.getSource()==caHomeButton){
            root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Menu-ClubAdvisor.fxml"));
        }if(actionEvent.getSource()==caEventsButton){
            root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Events-ClubAdvisor.fxml"));
        }if(actionEvent.getSource()==caClubsButton){
            root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Clubs-ClubAdvisor.fxml"));
        }if(actionEvent.getSource()==caReportsButton){
            root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Report-ClubAdvisor.fxml"));
        }
//        if(actionEvent.getSource()==caAccount){
//            root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Clubs-ClubAdvisor.fxml"));
//        }
        scene = new Scene(root);
        stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void newEventCreation(ActionEvent event){

        System.out.println(newEventNameField.getText());

        EventDataHandling eventDataHandling = new EventDataHandling();


        eventDataHandling.newEventCreationToDatabase(newEventIDField.getText(),newEventNameField.getText(),newEventVenueField.getText(),newEventDatePicker.getValue(), organizingClubComboBox.getValue(), newEventDescriptionTextArea.getText());

    }


    /**
     *
     */
    @FXML
    private void handleNewEventNameChange(){
        newEventNameField.textProperty().addListener(newEventNameFieldListener);
    }

    /**
     *
     */
    @FXML
    private void hanldeNewEventDescription(){
        newEventDescriptionTextArea.textProperty().addListener(newEventDescriptionTextAreaListener);
    }

    /**
     *
     */
    @FXML
    private void handleNewEventVenue(){
        newEventVenueField.textProperty().addListener(newEventDescriptionTextAreaListener);
    }

    public void searchEventUpdateAction(ActionEvent event) {
    }




    /*
        Updating the events
    */









}

