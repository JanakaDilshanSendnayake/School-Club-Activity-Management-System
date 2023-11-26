package SceneControlersClubAdvisors;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;
import stake_holders.ClubAdvisor;
import stake_holders.Clubs;
import stake_holders.Events;
import stake_holders.Student;
import utils.ClubDataHandling;
import utils.EventDataHandling;


import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @FXML private ComboBox<String> navigateEventsPageComboBox;
    ObservableList<String> navigateEventsPageComboBoxData= FXCollections.observableArrayList();

    //=============================================
    @FXML private Label eventNameLabel;
    @FXML private Label eventVenueLabel;
    @FXML private Label eventDateLabel;
    @FXML private Label eventOrganizingClubNmaeLabel;
    @FXML private TextArea eventDescriptionTextArea;

    @FXML private ComboBox<String> attendanceMarkingClubComboBox ;
    @FXML private ComboBox<String> attendanceMarkingEventComboBox;
    ObservableList<String> clubsData=FXCollections.observableArrayList();
    ObservableList<String> eventData=FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatus=false;
        newEventDescriptionTextArea.setWrapText(true);
        eventDescriptionTextArea.setWrapText(true);
        //caNaviEvents.toFront();

        ClubDataHandling object=new ClubDataHandling();
        object.loadClubDataRelevantToCA(Main.currentUser);

        ArrayList<String> namesOfClubsWithAdminAccess = new ArrayList<>();
        ArrayList<String> namesOfClubsWithoutAdminAccess=new ArrayList<>();

        for (Clubs ca : Main.currentUser.getClubsWithAdminAccess()) {
            namesOfClubsWithAdminAccess.add(ca.getClubId()+"-"+ca.getClubName());
        }
        for (Clubs ca : Main.currentUser.getClubsWithoutAdminAccess()){
            namesOfClubsWithoutAdminAccess.add(ca.getClubId()+"-"+ca.getClubName());
        }
        //Event Creation combo box===========================================================

        clubsSorterData.addAll(namesOfClubsWithAdminAccess);
        organizingClubComboBox.setItems(clubsSorterData);

        organizingClubComboBox.setOnAction(event->{
            String selectedOption = organizingClubComboBox.getSelectionModel().getSelectedItem();
//            setUpClubAdvisorMembersNaviTable(selectedOption);

            String[] split=selectedOption.split("-");

            ClubDataHandling obj=new ClubDataHandling();
            Main.currentClub= obj.loadClubData(split[0]);

            System.out.println(Main.currentClub.getClubName());
        });
        //Event navigation club box==========================================================
        navigateEventsPageComboBoxData.addAll(namesOfClubsWithAdminAccess);
        navigateEventsPageComboBoxData.addAll(namesOfClubsWithoutAdminAccess);
        navigateEventsPageComboBox.setItems(navigateEventsPageComboBoxData);

        navigateEventsPageComboBox.setOnAction(event->{
            String selectedOption = navigateEventsPageComboBox.getSelectionModel().getSelectedItem();
//            setUpClubAdvisorMembersNaviTable(selectedOption);

            if (selectedOption != null) {
                String[] split=selectedOption.split("-");
                ClubDataHandling obj=new ClubDataHandling();
                setUpEventNavigateTable(obj.loadClubData(split[0])) ;
            }

        });
        //Attendance marking=======================================================================================
        clubsData.addAll(namesOfClubsWithAdminAccess);
        clubsData.addAll(namesOfClubsWithoutAdminAccess);
        attendanceMarkingClubComboBox.setItems(clubsData);


        //attendanceMarkingEventComboBox.setItems(eventData);

        attendanceMarkingClubComboBox.setOnAction(event->{
            String selectedOption = attendanceMarkingClubComboBox.getSelectionModel().getSelectedItem();

            if (selectedOption != null) {
                String[] split=selectedOption.split("-");
                ClubDataHandling obj=new ClubDataHandling();
                Main.currentClub=obj.loadClubData(split[0]) ;

                ClubDataHandling obj1=new ClubDataHandling();
                obj1.loadClubStudentMembershipData(Main.currentClub);

                setUpRegistrationMarkingTable(Main.currentClub.getStudentMembers());

                EventDataHandling obj2=new EventDataHandling();
                obj2.loadEventDataOfAClub(Main.currentClub);

                ArrayList<String> namesOfEvents=new ArrayList<>();
                for(Events events:Main.currentClub.getClubEvents()){
                    namesOfEvents.add(events.getEventId()+"-"+events.getEventName());
                }
                eventData.setAll(namesOfEvents);
                attendanceMarkingEventComboBox.setItems(eventData);
            }

        });

        attendanceMarkingEventComboBox.setOnAction(event->{
            String selectedOption = attendanceMarkingEventComboBox.getSelectionModel().getSelectedItem();
//            setUpClubAdvisorMembersNaviTable(selectedOption);

            if (selectedOption != null) {
                String[] split=selectedOption.split("-");

            }

        });

        eventNavigateTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Main.currentClub=newSelection.getParentClub();
                eventNameLabel.setText(newSelection.getEventName());
                eventDescriptionTextArea.setText(newSelection.getEventDescription());
                eventDateLabel.setText(newSelection.getEventDate().toString());
                eventVenueLabel.setText(newSelection.getEventLocation());
                eventOrganizingClubNmaeLabel.setText(Main.currentClub.getClubName());

                updateEventNameField.setText(newSelection.getEventName());
                updateEventDescriptionTextArea.setText(newSelection.getEventDescription());
                updateEventDatePicker.setValue(newSelection.getEventDate());
                updateEventVenueField.setText(newSelection.getEventLocation());


            }
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
    @FXML private TableView<Events> eventNavigateTable;
    @FXML private TableColumn<Events,String> eventIdColumn;
    @FXML private TableColumn<Events,String> eventNameColumn;
    @FXML private TableColumn<LocalDate,String> eventDateColumn;
    @FXML private TableColumn<Events,String> eventVenueColumn;

    private void setUpEventNavigateTable(Clubs clubs){
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        eventVenueColumn.setCellValueFactory(new PropertyValueFactory<>("eventLocation"));

        EventDataHandling obj=new EventDataHandling();
        obj.loadEventDataOfAClub(clubs);
        ObservableList<Events> eventNavigateTableData=FXCollections.observableArrayList();
        try{eventNavigateTableData.addAll(clubs.getClubEvents());}catch (Exception e){
            showInfoAlert("There aren't any events under this club");
        }
        eventNavigateTable.setItems(eventNavigateTableData);
    }

    @FXML private TableView<Student> registrationMarkingTable;
    @FXML private TableColumn<Student,String> studentIdColumn;
    @FXML private TableColumn<Student,String> studentNameColumn;
    private void setUpRegistrationMarkingTable(ArrayList<Student> studentMembersArray){
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name") );
        ObservableList<Student> registrationMarkingTableData=FXCollections.observableArrayList();
        registrationMarkingTableData.addAll(studentMembersArray);
        registrationMarkingTable.setItems(registrationMarkingTableData);
        registrationMarkingTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }
    @FXML
    private void saveAttendance(){
        ArrayList<Student> selectedStudents = new ArrayList<>(registrationMarkingTable.getSelectionModel().getSelectedItems());
        for(Student student:selectedStudents){
            System.out.println("===================");
            System.out.println(student.getName());
        }
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
                //================================================

                ClubDataHandling obj=new ClubDataHandling();
                obj.loadClubStudentMembershipData(Main.currentClub);
                System.out.println("Student members-"+Main.currentClub.getStudentMembers().get(0).getStudentId());


                //===================================================================
                Events newEvent=Main.currentClub.createEvent(newEventId,newEventName,newEventDate,newEventVenue,newClubDescription);
                System.out.println(newEvent.getEventId());

                EventDataHandling object=new EventDataHandling();
                object.saveNewEventToDatabase(newEvent);

                showInfoAlert("Success");
            }catch (IllegalArgumentException e){
                showErrorAlert(e.toString());
            }

    }



    //When user selects an event from the table in event navigation page and press view button,
    //    data of that event will be loaded in the view event details page
    @FXML
    public void viewEventDetails(){
        caViewEvents.toFront();
        //Rest of the function
    }
    @FXML
    public void goBackToEventNavigation(){
        caNaviEvents.toFront();
    }
    //  When the user want to edit the current details of the event and click edit button,
    //  current data should be loaded in the editable text fields in update edit details

    private boolean updateStatus; // this variable is used to check whether if there is an ongoing detail update.
    @FXML
    private TextField updateEventNameField;
    @FXML
    private Label updateEventNameLabel;

    @FXML
    private TextArea updateEventDescriptionTextArea;
    @FXML
    private Label updateEventDescriptionLabel;
    @FXML
    private TextField updateEventVenueField;
    @FXML
    private Label updateEventVenueFieldLabel;
    @FXML
    private DatePicker updateEventDatePicker;
    @FXML
    private Label updateEventDatePickerLabel;
    @FXML
    public void editEventDetails(){
        updateStatus=true;

        ArrayList<String> clubAdminIds = new ArrayList<>();

        for (ClubAdvisor ca : Main.currentClub.getClubAdmin()) {
            clubAdminIds.add(ca.getClubAdvisorId());
        }
        if (clubAdminIds.contains(Main.currentUser.getClubAdvisorId())) {
            updateStatus = true;
            caUpdateEvents.toFront();
        } else {
            showErrorAlert("You aren't authorized for this action!");
        }
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

        String eventId=Main.currentEvent.getEventId();
        String updatedEventName=updateEventNameField.getText();
        String updatedEventVenue=updateEventVenueField.getText();
        String updatedEventDescription=updateEventDescriptionTextArea.getText();
        LocalDate updatedEventDate=updateEventDatePicker.getValue();

        Events eventObjectWithUpdatedDetails=new Events(eventId,updatedEventName,updatedEventDate,updatedEventVenue,updatedEventDescription,Main.currentClub);

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

