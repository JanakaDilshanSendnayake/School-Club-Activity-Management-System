package SceneControlersClubAdvisors;

import CommonSceneControlers.BaseSceneController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.Main;
import stake_holders.*;
import utils.ClubDataHandling;
import utils.EventDataHandling;


import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
    //Event creation,updating Manith Ratnayake RGU ID-2237949
    //Attendance tracking Abdul Shahid- RGU ID 2237030
public class CAEvents extends BaseSceneController implements Initializable {

//===============================================
    //Panes
    @FXML private TabPane caEventsTabPane;
    @FXML private Tab eventNaviTab;
    @FXML private Tab createNewEventTab;
    @FXML private AnchorPane caNaviEvents;
    @FXML private AnchorPane caViewEvents;
    @FXML private AnchorPane caUpdateEvents;
    @FXML private AnchorPane caCreateNewEvents;
//====================================================

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

    //==========================================================
    private ChangeListener<String> newEventNameFieldListener;
    private ChangeListener<String> newEventDescriptionTextAreaListener;
    private ChangeListener<String> newEventVenueFieldListener;
    //==============================================================
    private ChangeListener<String> updateEventNameFieldListener;
    private ChangeListener<String> updateEventDescriptionTextAreaListener;
    private ChangeListener<String> updateEventVenueFieldListener;
    //=============================================================

    private static final String EVENT_NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String EVENT_DESCRIPTION_REGEX = "^(?s).{1,200}$";

    private static final String EVENT_VENUE_REGEX = "^.+$";
    //================================================================

    //@FXML private ComboBox<String> eventCreatorClubSelector;
    ObservableList<String> clubsSorterData= FXCollections.observableArrayList();
    @FXML private ComboBox<String> navigateEventsPageComboBox;
    @FXML private ComboBox<String> navigateEventsPageComboBox2;
    ObservableList<String> navigateEventsPageComboBoxData= FXCollections.observableArrayList();
    ObservableList<String> navigateEventsPageComboBox2Data = FXCollections.observableArrayList(
            "All Clubs",
            "My Clubs With Admin Privileges",
            "My Clubs Without Admin Privileges"
    );

    //=============================================
    @FXML private Label eventNameLabel;
    @FXML private Label eventVenueLabel;
    @FXML private Label eventDateLabel;
    @FXML private Label eventOrganizingClubNmaeLabel;
    @FXML private TextArea eventDescriptionTextArea;

    //==============================================

    @FXML private ComboBox<String> attendanceMarkingClubComboBox ;
    @FXML private ComboBox<String> attendanceMarkingEventComboBox;
    ObservableList<String> clubsDataForAttendanceMarking =FXCollections.observableArrayList();
    ObservableList<String> eventDataForAttendanceMarking =FXCollections.observableArrayList();
    //===============================
    private String organizingClubComboBoxSelectedOption;
    private String navigateEventsPageComboBoxSelectedOption;
    private String attendanceMarkingClubComboBoxSelectedOption;
    private String attendanceMarkingEventComboBoxSelectedOption;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        caNaviEvents.toFront();
        updateStatus=false;
        newEventDescriptionTextArea.setWrapText(true);
        eventDescriptionTextArea.setWrapText(true);
        //caNaviEvents.toFront();

        ClubDataHandling object=new ClubDataHandling();
        object.loadClubDataRelevantToCA(Main.currentUser);

        ArrayList<String> namesOfClubsWithAdminAccess = new ArrayList<>();
        ArrayList<String> namesOfClubsWithoutAdminAccess=new ArrayList<>();
        ArrayList<String> namesOfAllClubs=new ArrayList<>();

        for (Clubs ca : Main.currentUser.getClubsWithAdminAccess()) {
            namesOfClubsWithAdminAccess.add(ca.getClubId()+"-"+ca.getClubName());
        }
        for (Clubs ca : Main.currentUser.getClubsWithoutAdminAccess()){
            namesOfClubsWithoutAdminAccess.add(ca.getClubId()+"-"+ca.getClubName());
        }
        try {
            for (Clubs ca: object.loadAllClubs()){
                namesOfAllClubs.add(ca.getClubId()+"-"+ca.getClubName());
            }
        }catch (SQLException e){
            showErrorAlert(e.getMessage());
        }


        //Setting up Event Creation combo box===========================================================

        clubsSorterData.addAll(namesOfClubsWithAdminAccess);
        organizingClubComboBox.setItems(clubsSorterData);

        organizingClubComboBox.setOnAction(event->{
            organizingClubComboBoxSelectedOption=organizingClubComboBox.getSelectionModel().getSelectedItem();
        });
        //Event navigation club box==========================================================

        //loading event Navigate table==============================================
        navigateEventsPageComboBox2.setItems(navigateEventsPageComboBox2Data);

        navigateEventsPageComboBox2.setOnAction(event->{
            object.loadClubDataRelevantToCA(Main.currentUser);

            String selectedOption = navigateEventsPageComboBox2.getSelectionModel().getSelectedItem();

            if (selectedOption.equals("All Clubs")){
                navigateEventsPageComboBoxData.clear();
                navigateEventsPageComboBoxData.addAll(namesOfAllClubs);
            } else if (selectedOption.equals("My Clubs With Admin Privileges")) {
                navigateEventsPageComboBoxData.clear();
                navigateEventsPageComboBoxData.addAll(namesOfClubsWithAdminAccess);
            } else if (selectedOption.equals("My Clubs Without Admin Privileges")) {
                navigateEventsPageComboBoxData.clear();
                navigateEventsPageComboBoxData.addAll(namesOfClubsWithoutAdminAccess);
            }
            navigateEventsPageComboBox.setItems(navigateEventsPageComboBoxData);
        });

        navigateEventsPageComboBox.setOnAction(event->{
            navigateEventsPageComboBoxSelectedOption = navigateEventsPageComboBox.getSelectionModel().getSelectedItem();

            if (navigateEventsPageComboBoxSelectedOption != null) {
                setUpEventNavigateTable(selectedClubOptionToClubObject(navigateEventsPageComboBoxSelectedOption)) ;
            }
        });




        //Attendance marking=======================================================================================
        clubsDataForAttendanceMarking.addAll(namesOfClubsWithAdminAccess);
        clubsDataForAttendanceMarking.addAll(namesOfClubsWithoutAdminAccess);
        attendanceMarkingClubComboBox.setItems(clubsDataForAttendanceMarking);


        //attendanceMarkingEventComboBox.setItems(eventData);

        attendanceMarkingClubComboBox.setOnAction(event->{
            attendanceMarkingClubComboBoxSelectedOption = attendanceMarkingClubComboBox.getSelectionModel().getSelectedItem();

            if (attendanceMarkingClubComboBoxSelectedOption != null) {
                String[] split=attendanceMarkingClubComboBoxSelectedOption.split("-");
                ClubDataHandling obj=new ClubDataHandling();
                Clubs selectedClub=selectedClubOptionToClubObject(attendanceMarkingClubComboBoxSelectedOption) ;

                ClubDataHandling obj1=new ClubDataHandling();
                obj1.loadClubStudentMembershipData(selectedClub);

                setUpRegistrationMarkingTable(selectedClub.getStudentMembers());

                EventDataHandling obj2=new EventDataHandling();
                obj2.loadEventDataOfAClub(selectedClub);

                ArrayList<String> namesOfEvents=new ArrayList<>();
                for(Events events:selectedClub.getClubEvents()){
                    namesOfEvents.add(events.getEventId()+"-"+events.getEventName());
                }
                eventDataForAttendanceMarking.setAll(namesOfEvents);
                attendanceMarkingEventComboBox.setItems(eventDataForAttendanceMarking);
            }

        });

        attendanceMarkingEventComboBox.setOnAction(event->{
            attendanceMarkingEventComboBoxSelectedOption = attendanceMarkingEventComboBox.getSelectionModel().getSelectedItem();
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
//Event listeners for event creation page=======================================================================
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
//Event listeners for event update page=======================================================================

        updateEventNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, EVENT_NAME_REGEX,
                    "Name can only contain letters and Underscores.",30);
            updateEventNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, updateEventNameLabel);
        });


        // initializing the event listener to validate the user input in "newClubNameField" text-field
        updateEventDescriptionTextAreaListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, EVENT_DESCRIPTION_REGEX,
                    "",500);
            updateEventDescriptionLabel.setText(validationMessage);
            setLabelStyle(validationMessage, updateEventDescriptionLabel);
        });

        // initializing the event listener to validate the user input in "newClubNameField" text-field
        updateEventVenueFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, EVENT_VENUE_REGEX,
                    "This field is mandatory.");
            updateEventVenueFieldLabel.setText(validationMessage);
            setLabelStyle(validationMessage, updateEventVenueFieldLabel);
        });
    }
    private Clubs selectedClubOptionToClubObject(String selectedClubOption){
        String[] split=selectedClubOption.split("-");
        ClubDataHandling obj=new ClubDataHandling();
        return obj.loadClubData(split[0]);
    }
    private Events selectedEventOptionToObject(String selectedEventOption){
        String[] split=selectedEventOption.split("-");
        EventDataHandling obj=new EventDataHandling();
        return obj.loadEventData(split[0]);
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



        if(attendanceMarkingClubComboBoxSelectedOption==null){
            showErrorAlert("Please select the club and the relevant event.");
        }else{

            if(attendanceMarkingEventComboBoxSelectedOption==null){
                showErrorAlert("Please select the events you are marking the attendance for");
            }else{
                Clubs attendanceMarkingClub=selectedClubOptionToClubObject(attendanceMarkingClubComboBoxSelectedOption);
                Events attendanceMarkingEvent=selectedEventOptionToObject(attendanceMarkingEventComboBoxSelectedOption);

                //Getting selected students
                ArrayList<Student> selectedStudents = new ArrayList<>(registrationMarkingTable.getSelectionModel().getSelectedItems());

                //getting unselected students
                ArrayList<Student> unselectedStudents= new ArrayList<>(registrationMarkingTable.getItems());
                unselectedStudents.removeAll(selectedStudents);

                EventDataHandling obj=new EventDataHandling();
                if(obj.attendanceAlreadyCheckedOrNot(attendanceMarkingEvent.getEventId())){
                    showInfoAlert("You have already marked the attendance for this Event.");
                }else if(showConfirmationAlert("Are you sure that you want to save attendance for this event? One it's done you can't change it.")){
                    for(Student student:selectedStudents){
                        Attendance attendance=new Attendance(student,attendanceMarkingEvent,true);
                        obj.markAttendance(attendance);
                    }
                    for(Student student:unselectedStudents){
                        Attendance attendance=new Attendance(student,attendanceMarkingEvent,false);
                        obj.markAttendance(attendance);
                    }
                }
            }
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
//===================================================================
    @FXML
    private void handleUpdateEventName(){
        updateEventNameField.textProperty().addListener(updateEventNameFieldListener);
    }
    @FXML
    private void handleUpdateEventVenue(){
        updateEventVenueField.textProperty().addListener(updateEventVenueFieldListener);
    }
    @FXML
    private void handleUpdateEventDescription(){
        updateEventDescriptionTextArea.textProperty().addListener(updateEventDescriptionTextAreaListener);
    }
    private String generateEventId() {
        Clubs clubs=selectedClubOptionToClubObject(organizingClubComboBoxSelectedOption);
        String prefix = "E";
        int randomNumber;
        EventDataHandling object=new EventDataHandling();

        do {
            randomNumber = new Random().nextInt(1000);
        } while (object.validateEventId(clubs.getClubId()+prefix + randomNumber));

        return clubs.getClubId()+prefix + randomNumber;
    }
    @FXML
    private void saveEvent(){
        //if(!(organizingClubComboBox.getSelectionModel().getSelectedItem()==null)){
            String newEventId=generateEventId();
            String newEventName= newEventNameField.getText();
            String newClubDescription=newEventDescriptionTextArea.getText();
            String newEventVenue=newEventVenueField.getText();
            LocalDate newEventDate=newEventDatePicker.getValue();


            Clubs currentClubUserCreatingTheEventFor= selectedClubOptionToClubObject(organizingClubComboBoxSelectedOption);
            try{
                //===================================================================
                Events newEvent=currentClubUserCreatingTheEventFor.createEvent(newEventId,newEventName,newEventDate,newEventVenue,newClubDescription);
                EventDataHandling object=new EventDataHandling();
                object.saveNewEventToDatabase(newEvent);

                showInfoAlert("Success");
                clearFields();
            }catch (IllegalArgumentException e){
                showErrorAlert(e.getMessage());
            }

    }
    @FXML
    private void clearFields(){
        newEventNameField.textProperty().removeListener(newEventNameFieldListener);
        newEventVenueField.textProperty().removeListener(newEventVenueFieldListener);
        newEventDescriptionTextArea.textProperty().removeListener(newEventDescriptionTextAreaListener);
        newEventNameField.setText("");
        newEventDescriptionTextArea.setText("");
        newEventVenueField.setText("");
        newEventNameLabel.setText("");
        newEventVenueFieldLabel.setText("");
        newEventDescriptionLabel.setText("");
    }

//Event Navigation and management=======================================================================================

    //When user selects an event from the table in event navigation page and press view button,
    //    data of that event will be loaded in the view event details page
    @FXML
    public void viewEventDetails(){
        Events theEventUserIsViewing=eventNavigateTable.getSelectionModel().getSelectedItem();

        if(theEventUserIsViewing==null){
            showErrorAlert("please select an event from the table");
        }else{
            //set up the text labels
            eventNameLabel.setText(theEventUserIsViewing.getEventName());
            eventDescriptionTextArea.setText(theEventUserIsViewing.getEventDescription());
            eventDateLabel.setText(theEventUserIsViewing.getEventDate().toString());
            eventVenueLabel.setText(theEventUserIsViewing.getEventLocation());
            eventOrganizingClubNmaeLabel.setText(theEventUserIsViewing.getParentClub().getClubName());
            //loading the pane
            caViewEvents.toFront();
        }
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

        Clubs clubTheEventBelogsTo= selectedClubOptionToClubObject(navigateEventsPageComboBoxSelectedOption);

        ClubDataHandling obj=new ClubDataHandling();
        obj.loadClubMembershipData(clubTheEventBelogsTo);

        ArrayList<String> clubAdminIds = new ArrayList<>();

        for (ClubAdvisor ca : clubTheEventBelogsTo.getClubAdmin()) {
            clubAdminIds.add(ca.getClubAdvisorId());
        }
        if (clubAdminIds.contains(Main.currentUser.getClubAdvisorId())) {
            updateStatus = true;
            Events theEventUserIsUpdating=eventNavigateTable.getSelectionModel().getSelectedItem();
            //set up the data in text labels
            updateEventNameField.setText(theEventUserIsUpdating.getEventName());
            updateEventDescriptionTextArea.setText(theEventUserIsUpdating.getEventDescription());
            updateEventDatePicker.setValue(theEventUserIsUpdating.getEventDate());
            updateEventVenueField.setText(theEventUserIsUpdating.getEventLocation());
            //loading the pane
            caUpdateEvents.toFront();
        } else {
            showErrorAlert("You aren't authorized for this action!");
        }
    }

    //  When user inputs updated data and press update button, data should be validated and saved
    @FXML
    public void updateEventDetails(){

        Events theEventUserUpdated=eventNavigateTable.getSelectionModel().getSelectedItem();


        String updatedEventName=updateEventNameField.getText();
        String updatedEventVenue=updateEventVenueField.getText();
        String updatedEventDescription=updateEventDescriptionTextArea.getText();
        LocalDate updatedEventDate=updateEventDatePicker.getValue();


        theEventUserUpdated.setEventName(updatedEventName);
        theEventUserUpdated.setEventLocation(updatedEventVenue);
        theEventUserUpdated.setEventDescription(updatedEventDescription);
        theEventUserUpdated.setEventDate(updatedEventDate);

        //setup the method save updated event object in DB
        EventDataHandling obj=new EventDataHandling();
        obj.updateEventInDatabase(theEventUserUpdated);


        //Rest of the function
        updateStatus=false;
        showInfoAlert("Event details updated successfully");
        caViewEvents.toFront();
    }
    @FXML
    public void updateCancel(){
        if(showConfirmationAlert("Are you sure that you want to cancel the update?")) {
            updateStatus = false;
            caViewEvents.toFront();
        }
    }




}

