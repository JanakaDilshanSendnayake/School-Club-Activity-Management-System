package SceneControlersClubAdvisors;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.MySqlConnect;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CAEvents implements Initializable {

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

    //< shahids
    MySqlConnect ConnectNow = new MySqlConnect();
    ;Connection ConnectDB = ConnectNow.getDatabaseLink();

    @FXML
    private TableColumn<EventMarking, CheckBox> Attendencecol;

    @FXML
    private ComboBox<String> ClubSelectionChoiceBox;

    @FXML
    private ComboBox<String> EventSelectionChoiceBox;

    @FXML
    private TableColumn<EventMarking, String> StudentIDcol;

    @FXML
    private TableColumn<EventMarking, String> SturdentNamecol;

    @FXML
    private TableView<EventMarking> Tableview;

    @FXML
    private AnchorPane sidebar;
    //public Connection databaseLink;

    //ObservableList<EventMarking> events;
    EventMarking eventMarking = new EventMarking();
    // shahids>


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatus=false;
        //caNaviEvents.toFront();


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

        // < shahids initialize
        shahidsInitialize();
        /*try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/scams", "root", "#Wolf8me");
            PreparedStatement ps = ConnectDB.prepareStatement("select club_name from Clubs;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ClubSelectionChoiceBox.getItems().addAll(rs.getString("club_name"));}
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        // shahids initialize >

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
    private void handleNewEventVenue(){
        newEventVenueField.textProperty().addListener(newEventDescriptionTextAreaListener);
    }





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
    //    When user selects a event from the table in event navigation page and press view button,
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

    // < shahids functions

    void shahidsInitialize(){

        //EventMarking eventMarking = new EventMarking();
        ObservableList<String> clubChoices = eventMarking.getClubNames(ConnectDB);
        /*try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/scams", "root", "#Wolf8me");
            ObservableList<String> clubChoices = FXCollections.observableArrayList();
            PreparedStatement ps = ConnectDB.prepareStatement("select club_name from Clubs;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                clubChoices.add(rs.getString("club_name"));}
                //ClubSelectionChoiceBox.getItems().addAll(rs.getString("club_name"));}


        } catch (Exception e) {
            e.printStackTrace();
        }*/
        ClubSelectionChoiceBox.setItems(clubChoices);
    }

    @FXML
    void EditTableview(ActionEvent event)  {
// start from here: he chooses club ---> display events for that club ----> update tableview
        String clubName = ClubSelectionChoiceBox.getValue();
        ObservableList<String> eventChoices = eventMarking.getEventNames(ConnectDB,clubName);
        EventSelectionChoiceBox.setItems(eventChoices);
        //ObservableList<String> eventChoices = FXCollections.observableArrayList();
       // EventSelectionChoiceBox.getItems().clear();
        //ObservableList<EventMarking> events = FXCollections.observableArrayList();

        //List<CheckBox> checkBoxes = new ArrayList<>();
        /*try {
            PreparedStatement ps = ConnectDB.prepareStatement("select event_name\n" +
                    "from Event\n" +
                    "inner join Club\n" +
                    "on Event.club_id = Club.club_id\n" +
                    "where club_name = \"club_name\";");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventChoices.add(rs.getString("event_name"));
                //EventSelectionChoiceBox.getItems().addAll(rs.getString("event_name"));
            }*/
        /*try{
            PreparedStatement ps1 = ConnectDB.prepareStatement("select student.Student_id, student_name\n" +
                    "from Student_club\n" +
                    "inner join student\n" +
                    "on Student_club.student_id = student.student_id\n" +
                    "inner join club\n" +
                    "on student_club.club_id = club.club_id\n" +
                    "where club_name = \"club_name\";");
            ResultSet rs1 = ps1.executeQuery();
            while(rs1.next()){
                CheckBox test = new CheckBox();
                events.add(new EventMarking(rs1.getString("userID"),rs1.getString("name1"),test));
                //checkBoxes.add(test);
            }
        }catch (Exception e){
            e.printStackTrace();}*/
        ObservableList<EventMarking> register= eventMarking.createRegister(ConnectDB,clubName);

        StudentIDcol.setCellValueFactory(new PropertyValueFactory<EventMarking,String>("StudentID"));
        SturdentNamecol.setCellValueFactory(new PropertyValueFactory<EventMarking,String>("StudentName"));
        Attendencecol.setCellValueFactory(new PropertyValueFactory<EventMarking,CheckBox>("checkBox"));
        Tableview.setItems(register);
    }

    @FXML
    void handleSave(ActionEvent event) throws SQLException {
        String clubname = ClubSelectionChoiceBox.getValue();
        String eventName = EventSelectionChoiceBox.getValue();
        if (!ClubSelectionChoiceBox.getSelectionModel().isEmpty() && !EventSelectionChoiceBox.getSelectionModel().isEmpty() ){
//get event id
            eventMarking.saveAttendence(ConnectDB,eventName);
        /*PreparedStatement statement = ConnectDB.prepareStatement("select event_id \n" +
                "from event\n" +
                "where event_name = \"event_name\";");
        ResultSet event_id = statement.executeQuery();
// clear previos values
        PreparedStatement ps = ConnectDB.prepareStatement("delete\n" +
                "from attendance\n" +
                "where event_id = \"event_id\";");
        ps.executeUpdate();
// update attendence table
        //List<String> presentStudents = new ArrayList<>();
        for (EventMarking student:events) {
            if(student.getCheckBox().isSelected()){
                PreparedStatement ps1 = ConnectDB.prepareStatement("insert into attendance(student_id, event_id, attendance_status) values (student_id, event_id, attendance_status);");
                ps1.executeUpdate();// insert bool = True

                //presentStudents.add(student.getStudentName()+" "+student.getStudentID());
            }else{PreparedStatement ps1 = ConnectDB.prepareStatement("insert into attendance(student_id, event_id, attendance_status) values (student_id, event_id, attendance_status);");
                ps1.executeUpdate();}// insert bool = False
        }*/
        }else {Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Event not selected");
            alert.showAndWait();}
    }
    // shahids fuctions>
}

