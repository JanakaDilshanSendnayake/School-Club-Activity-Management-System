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
    @FXML
    private ComboBox<String> selectClubComboBoxUpdate;
    // Event Objects
    private ChangeListener<String> newEventNameFieldListener;
    private ChangeListener<String> newEventDescriptionTextAreaListener;
    private ChangeListener<String> newEventVenueFieldListener;

    // this variable is used to check whether if there is an ongoing detail update.
    private boolean updateStatus;

    int newEventID ;
    String newEventName;
    String newEventVenue;
    LocalDate newEventDate;

    String oldEventName;
    String newEventDescriptionText;

    String newselectClubComboBoxUpdate;
    String selectClubComboBoxUpdatePass;


    @FXML
    private ComboBox<String> viewEventOptionCA;


    public void initialize(URL url, ResourceBundle resourceBundle) {


        updateStatus = false;

        caEventsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (updateStatus && newTab == createNewEventTab) {
                if (showConfirmationAlert("There's an ongoing update. Do you want to cancel it?")) {
                    caEventsTabPane.getSelectionModel().select(newTab);
                } else {
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


        // 1st Pane = Update events and Navigation of events

        // Getting all the available clubs from database to club list combobox
        EventDataHandling eventDataHandling = new EventDataHandling();

        //Calling the database class and retrieve club list to a observableList
        ObservableList<String> clubList = eventDataHandling.getClubListFromDatabase();

        // Navigate and manage event combobox setting entire club-list
        selectClubComboBox.setItems(clubList);

        // Create a new event combobox setting entire club-list
        organizingClubComboBox.setItems(clubList);

        // Navigate and manage event combobox setting entire club-list ; If the club advisor want to update, will choose
        selectClubComboBoxUpdate.setItems(clubList);


        //(Navigation and Update Pane only ) After Club advisor selects the required club, all the available events are shown on event combo box
        selectClubComboBox.setOnAction(event -> {
            String selectedClub = selectClubComboBox.getValue();
            if (selectedClub != null) {
                System.out.println("Selected Club: " + selectedClub);

                ObservableList<String> events = eventDataHandling.getEventListByClub(selectedClub);
                selectEventComboBox.setItems(events);


            } else {
                System.out.println("No club selected.");
            }
        });


        // After club advisor selects the event all the event details will be automatically displayed on the relevant field
        selectEventComboBox.setOnAction(event -> {
            String selectedEvent = selectEventComboBox.getValue();

            if (selectedEvent != null) {
                System.out.println("Selected Event: " + selectedEvent);

                // Taking all the information from database ( event_id, event_name , event_venue etc ) to a observable list
                ObservableList<Object> observableList = eventDataHandling.getEventsByEventName(selectedEvent);

                for (Object obj : observableList) {
                    System.out.println(observableList);
                }


                System.out.println("right there " + observableList.get(4));

                // old data
                oldEventName = (String) observableList.get(1);

                newEventID = (Integer) observableList.get(0);
                newEventName = (String) observableList.get(1);
                newEventVenue = (String) observableList.get(2);
                newEventDate = (LocalDate) observableList.get(3);
                newEventDescriptionText = (String) observableList.get(4);

                // This is club id : Which we are turning to relavent name and again turning to club id when storing
                newselectClubComboBoxUpdate = (String) observableList.get(5);

                System.out.println("right here " + newselectClubComboBoxUpdate);

                newEventIDFieldUpdate.setText(String.valueOf(newEventID));
                newEventNameFieldUpdate.setText(newEventName);
                newEventVenueFieldUpdate.setText(newEventVenue);
                newEventDatePickerUpdate.setValue(newEventDate);
                newEventDescriptionTextAreaUpdate.setText(newEventDescriptionText);


            } else {
                System.out.println("No club selected.");
            }
        });

//        eventDataHandling.getEventListFromDatabase();

        viewEventOptionCA.getItems().addAll("All Events", "My Events Only", "Other events");

        viewEventOptionCA.setOnAction(event -> {
            String selectedOption = viewEventOptionCA.getValue();
            System.out.println("Selected option: " + selectedOption);

            // Perform actions based on the selected option
            if ("All Events".equals(selectedOption)) {
                // Do something for Option 1
            } else if ("My Events Only".equals(selectedOption)) {
                // Do something for Option 2
            } else if ("Other events".equals(selectedOption)) {
                // Do something for Option 3
            }
        });

//        TableView<Event> tableView = new TableView<>();
//
//        // Define columns
//        TableColumn<Event, String> firstNameColumn = new TableColumn<>("First Name");
//        TableColumn<Person, String> lastNameColumn = new TableColumn<>("Last Name");
//        TableColumn<Person, Integer> ageColumn = new TableColumn<>("Age");
//
//        // Set cell value factories
//        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
//        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
//        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
//
//        // Add columns to the table
//        tableView.getColumns().addAll(firstNameColumn, lastNameColumn, ageColumn)
//





    }

    // After the club advisor has filled the form to save data
    @FXML
    public void newEventCreation(ActionEvent event){

        newEventIDFieldUpdate.setText(String.valueOf(newEventID));
        newEventNameFieldUpdate.setText(newEventName);
        newEventVenueFieldUpdate.setText(newEventVenue);
        newEventDatePickerUpdate.setValue(newEventDate);
        newEventDescriptionTextAreaUpdate.setText(newEventDescriptionText);

        EventDataHandling eventDataHandling = new EventDataHandling();
        eventDataHandling.newEventCreationToDatabase(newEventIDField.getText(),newEventNameField.getText(),newEventVenueField.getText(),newEventDatePicker.getValue(), newEventDescriptionTextArea.getText(),organizingClubComboBox.getValue());

    }

    /**
     * When the club advisor decided to update the edited event details it will send to the database
     * @param event
     * @throws SQLException
     */
    public void eventUpdateAction(ActionEvent event) throws SQLException {

        String newEventIDFieldUpdatePass = newEventIDFieldUpdate.getText();
        String newEventNameFieldUpdatePass = newEventNameFieldUpdate.getText();
        String newEventVenueFieldUpdatePass = newEventVenueFieldUpdate.getText();
        LocalDate newEventDatePickerUpdatePass = newEventDatePickerUpdate.getValue();
        String newEventDescriptionTextAreaUpdatePass = newEventDescriptionTextAreaUpdate.getText();
        String selectClubComboBoxUpdatePass = selectClubComboBoxUpdate.getValue();

        System.out.println(selectClubComboBoxUpdatePass);
        System.out.println(newEventNameFieldUpdatePass);
        System.out.println(newEventVenueFieldUpdatePass);
        System.out.println(newEventDatePickerUpdatePass);
        System.out.println(newEventDescriptionTextAreaUpdatePass);
        System.out.println(selectClubComboBoxUpdatePass);


        EventDataHandling eventDataHandlingUpdate = new EventDataHandling();

        // Taking the club id of current selected class
        String club_idPass= eventDataHandlingUpdate.clubNameToClubID(selectClubComboBoxUpdatePass);


        System.out.println("Here : " + club_idPass);
        eventDataHandlingUpdate.UpdateEventTable(newEventIDFieldUpdatePass,newEventNameFieldUpdatePass,newEventVenueFieldUpdatePass,newEventDatePickerUpdatePass,newEventDescriptionTextAreaUpdatePass, club_idPass);

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
        Viewing the events
    */








}

