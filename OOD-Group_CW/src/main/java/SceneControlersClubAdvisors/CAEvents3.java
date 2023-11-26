package SceneControlersClubAdvisors;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
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
import main.Main;
import stake_holders.Clubs;
import stake_holders.Events;
import utils.ClubDataHandling;
import utils.EventDataHandling;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class CAEvents3 extends BaseSceneController implements Initializable {

    //Buttons - SideBar
    @FXML private Button caHomeButton;
    @FXML private Button caClubsButton;
    @FXML private Button caEventsButton;
    @FXML private Button caReportsButton;
    @FXML private Button caAccountButton;

    //Buttons
    @FXML private Button viewButton;
    @FXML private Button updateEventButton;
    @FXML private Button updateEventCancelButton;
    @FXML private Button editEventButton;
    @FXML private Button suspendEventButton;
    //@FXML private Button leaveClubButton;
    @FXML private Button backToClubNaviButton;


    //Panes
    @FXML private TabPane caEventsTabPane;
    @FXML private Tab eventNaviTab;
    @FXML private Tab createNewEventTab;
    @FXML private AnchorPane caNaviEvents;
    @FXML private AnchorPane caViewEvents;
    @FXML private AnchorPane caUpdateEvents;
    @FXML private AnchorPane caCreateNewEvents;

    //====
    private Stage stage;
    private Scene scene;
    private Parent root;

    //To validate the data inputs in Create new event tab
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

    private ChangeListener<String> newEventNameFieldListener;
    private ChangeListener<String> newEventDescriptionTextAreaListener;
    private ChangeListener<String> newEventVenueFieldListener;

    private static final String EVENT_NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String EVENT_DESCRIPTION_REGEX = "^(?s).{1,200}$";

    private static final String EVENT_VENUE_REGEX = "^.+$";

    //@FXML private ComboBox<String> eventCreatorClubSelector;
    ObservableList<String> clubsSorterData= FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatus=false;
        newEventDescriptionTextArea.setWrapText(true);
        //caNaviEvents.toFront();

        ClubDataHandling object=new ClubDataHandling();
        object.loadClubDataRelevantToCA(Main.currentUser);

        ArrayList<String> clubNames = new ArrayList<>();

        for (Clubs ca : Main.currentUser.getClubsWithAdminAccess()) {
            clubNames.add(ca.getClubId()+"-"+ca.getClubName());
        }

        clubsSorterData.addAll(clubNames);
        organizingClubComboBox.setItems(clubsSorterData);

        organizingClubComboBox.setOnAction(event->{
            String selectedOption = organizingClubComboBox.getSelectionModel().getSelectedItem();
//            setUpClubAdvisorMembersNaviTable(selectedOption);

            String[] split=selectedOption.split("-");

            ClubDataHandling obj=new ClubDataHandling();
            Main.currentClub= obj.loadClubData(split[0]);

            System.out.println(Main.currentClub.getClubName());
        });



//======================================================================================================================
        caEventsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (updateStatus && newTab == createNewEventTab) {
                if(showConfirmationAlert("There's an ongoing update. Do you want to cancel it?")){
                    caEventsTabPane.getSelectionModel().select(newTab);
                }else{
                    caEventsTabPane.getSelectionModel().select(oldTab);
                }

            }
        });
        // initializing the event listener to validate the user input in "newClubNameField" text-field
        newEventNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, EVENT_NAME_REGEX,
                    "Name can only contain letters and Underscores.",30);
            newEventNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newEventNameLabel);
        });


        // initializing the event listener to validate the user input in "newClubNameField" text-field
        newEventDescriptionTextAreaListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, EVENT_DESCRIPTION_REGEX,
                    "",500);
            newEventDescriptionLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newEventDescriptionLabel);
        });

        // initializing the event listener to validate the user input in "newClubNameField" text-field
        newEventVenueFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, EVENT_VENUE_REGEX,
                    "This field is mandatory.");
            newEventVenueFieldLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newEventVenueFieldLabel);
        });

    }

    @FXML
    private void handleNewEventNameChange(){
        newEventNameField.textProperty().addListener(newEventNameFieldListener);
    }

    @FXML
    private void hanldeNewEventDescription(){
        newEventDescriptionTextArea.textProperty().addListener(newEventDescriptionTextAreaListener);
    }

    @FXML
    private void handleNewEventVenue() {
        newEventVenueField.textProperty().addListener(newEventVenueFieldListener);
    }

    private String generateEventId() {
        String prefix = "E";
        int randomNumber;
        EventDataHandling object=new EventDataHandling();

        do {
            randomNumber = new Random().nextInt(1000);
        } while (object.validateEventId(Main.currentClub.getClubId()+prefix + randomNumber));

        return Main.currentClub.getClubId()+prefix + randomNumber;
    }
    @FXML
    private void saveEvent(){
        //if(!(organizingClubComboBox.getSelectionModel().getSelectedItem()==null)){
            String newEventId=generateEventId();
            String newEventName= newEventNameField.getText();
            String newClubDescription=newEventDescriptionTextArea.getText();
            String newEventVenue=newEventVenueField.getText();
            LocalDate newEventDate=newEventDatePicker.getValue();

            System.out.println(Main.currentClub.getClubName());

            System.out.println(newEventId);
            System.out.println(newEventName);
            System.out.println(newClubDescription);
            System.out.println(newEventVenue);
            System.out.println(newEventDate);

            try{
                Events newEvent=Main.currentClub.createEvent(newEventId,newEventName,newEventDate,newEventVenue,newClubDescription);
                System.out.println(newEvent.getEventId());

                EventDataHandling object=new EventDataHandling();
                object.saveNewEventToDatabase(newEvent);

                showInfoAlert("Success");
            }catch (IllegalArgumentException e){
                showErrorAlert(e.toString());
            }
        //}

    }



    //When user selects an event from the table in event navigation page and press view button,
    //    data of that event will be loaded in the view event details page
    @FXML
    public void viewClubDetails(){
        caViewEvents.toFront();
        //Rest of the function
    }
    //  When the user want to edit the current details of the event and click edit button,
    //  current data should be loaded in the editable text fields in update edit details

    private boolean updateStatus; // this variable is used to check whether if there is an ongoing detail update.
    @FXML
    public void editEventDetails(){
        updateStatus=true;
        caUpdateEvents.toFront();
        //Rest of the function
    }
    //  When user click suspend button the club and all the relevant information should be deleted
    @FXML
    public void suspendEvent(){
        if (showConfirmationAlert("Are you sure you want to suspend event.")){
            //Implement the rest of the function
            showInfoAlert("Event and all of the relevant details Successfully deleted.");
        }else{}
    }

    //  When user inputs updated data and press update button, data should be validated and saved
    @FXML
    public void updateEventDetails(){

        //Rest of the function
        updateStatus=false;
        showInfoAlert("Event details updated successfully");
        caViewEvents.toFront();
    }
    @FXML
    public void updateCancel(){

        // Rest of the function
        updateStatus=false;
        showErrorAlert("Update cancelled");
        caViewEvents.toFront();
    }

}

