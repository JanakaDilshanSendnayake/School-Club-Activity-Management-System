package SceneControlersClubAdvisors;

import CommonSceneControlers.BaseSceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;
import stake_holders.Attendance;
import stake_holders.Clubs;
import stake_holders.Events;
import stake_holders.Student;
import utils.ClubDataHandling;
import utils.EventDataHandling;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Attendance report-Abdul Shahid RGU:2237030 /
 * Club Membership Report- Akshith Rajendran RGU: 2236761 /
 * club activity report-Manith Ratnayake RGU: 2237949 /
 *
 */


public class CAReport extends BaseSceneController implements Initializable {

    @FXML
    private TableView<Student> studentMembersReportTable;
    @FXML private TableColumn<Student,String> studentIdColumn;
    @FXML private TableColumn<Student,String> studentNameColumn;
    @FXML private TableColumn<Student,String> studentEmailColumn;
    @FXML private TableColumn<Student,String> studentMobileNumberColumn;

    @FXML private ComboBox<String> clubsSorterInStudentMemberReport;
    ObservableList<String> clubsSorterInStudentMemberReportData =FXCollections.observableArrayList();
    @FXML private ComboBox<String> clubSorterInActivityReport;
    ObservableList<String> clubSorterInActivityReportData =FXCollections.observableArrayList();
    @FXML private ComboBox<String> clubSorterInAttendanceReport;
    ObservableList<String> clubSorterInAttendanceReportData = FXCollections.observableArrayList();
    @FXML private ComboBox<String> eventSorterInAttendanceReport;
    ObservableList<String> eventSorterInAttendanceReportData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ClubDataHandling object=new ClubDataHandling();
        object.loadClubDataRelevantToCA(Main.currentUser);
        System.out.println(Main.currentUser.getClubsWithAdminAccess());

        ArrayList<String> clubNames = new ArrayList<>();

        for (Clubs ca : Main.currentUser.getClubsWithAdminAccess()) {
            clubNames.add(ca.getClubId()+"-"+ca.getClubName());
        }
        for (Clubs ca : Main.currentUser.getClubsWithoutAdminAccess()) {
            clubNames.add(ca.getClubId()+"-"+ca.getClubName());
        }

        //Club membership report=============================================

        clubsSorterInStudentMemberReportData.addAll(clubNames);
        clubsSorterInStudentMemberReport.setItems(clubsSorterInStudentMemberReportData);

        clubsSorterInStudentMemberReport.setOnAction(event->{
            String selectedOption = clubsSorterInStudentMemberReport.getSelectionModel().getSelectedItem();
            setUpClubMembersReportTable(selectedOption);
        });
        //Club activity report============================================

        clubSorterInActivityReportData.addAll(clubNames);
        clubSorterInActivityReport.setItems(clubSorterInActivityReportData);

        clubSorterInActivityReport.setOnAction(event->{
            String selectedOption = clubSorterInActivityReport.getSelectionModel().getSelectedItem();
            setUpClubActivityReport(selectedOption);
        });
        //Event Attendance report============================================
        clubSorterInAttendanceReportData.addAll(clubNames);
        clubSorterInAttendanceReport.setItems(clubSorterInAttendanceReportData);

        clubSorterInAttendanceReport.setOnAction(event->{
            String selectedOption = clubSorterInAttendanceReport.getSelectionModel().getSelectedItem();
            Clubs clubs=selectedClubOptionToClubObject(selectedOption);

            EventDataHandling object2=new EventDataHandling();
            object2.loadEventDataOfAClub(clubs);

            ArrayList<String> namesOfEvents=new ArrayList<>();
            for(Events events:clubs.getClubEvents()){
                namesOfEvents.add(events.getEventId()+"-"+events.getEventName());
            }
            eventSorterInAttendanceReportData.clear();
            eventSorterInAttendanceReportData.addAll(namesOfEvents);
            eventSorterInAttendanceReport.setItems(eventSorterInAttendanceReportData);
        });

        eventSorterInAttendanceReport.setOnAction(event->{
            String selectedOption = eventSorterInAttendanceReport.getSelectionModel().getSelectedItem();
            Events events=selectedEventOptionToObject(selectedOption);
            EventDataHandling obj=new EventDataHandling();
            ArrayList<Attendance> attendances=new ArrayList<>();
            try {
                attendances=obj.loadAttendance(events);

            }catch (Exception e){

            }setAttendanceReport(attendances);


        });




    }
    @FXML private TableView<Attendance> attendanceReportTable;
    @FXML private TableColumn<Attendance,String> attendanceReportTableStudentIdColumn;
    @FXML private TableColumn<Attendance,String> attendanceReportTableStudentNameColumn;
    @FXML private TableColumn<Attendance,String> attendanceReportTableEventIdColumn;
    @FXML private TableColumn<Attendance,String> attendanceReportTableEventNameColumn;
    @FXML private TableColumn<Attendance,String> attendanceReportTableStatusColumn;

    private void setAttendanceReport(ArrayList<Attendance> arrayList){
        attendanceReportTableStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        attendanceReportTableStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        attendanceReportTableEventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        attendanceReportTableEventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        attendanceReportTableStatusColumn.setCellValueFactory(new PropertyValueFactory<>("verbalStatus"));

        ObservableList<Attendance> attendancesToDisplay= FXCollections.observableArrayList();
        attendancesToDisplay.addAll(arrayList);
        attendanceReportTable.setItems(attendancesToDisplay);
    }




    private void setUpClubMembersReportTable(String idAndName){
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentMobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));


        Clubs club=selectedClubOptionToClubObject(idAndName);
        ClubDataHandling object=new ClubDataHandling();
        object.loadClubStudentMembershipData(club);

        ObservableList<Student> studentMembersToDisplay= FXCollections.observableArrayList();

        try{studentMembersToDisplay.addAll(club.getStudentMembers());}catch (Exception e){
            System.out.println(e.getMessage());
        }
        studentMembersReportTable.setItems(studentMembersToDisplay);

    }
    @FXML private TableView<Events> eventTable;
    @FXML private TableColumn<Events,String> eventIdColumn;
    @FXML private TableColumn<Events,String> eventNameColumn;
    @FXML private TableColumn<LocalDate,String> eventDateColumn;
    @FXML private TableColumn<Events,String> eventVenueColumn;
    @FXML private TableColumn<Events,String> eventDescriptionColumn;

    private Clubs selectedClubOptionToClubObject(String selectedClubOption){
        ClubDataHandling obj=new ClubDataHandling();
        Clubs selectedClub = null;
        try{
            String[] split=selectedClubOption.split("-");
            selectedClub=obj.loadClubData(split[0]);
        }catch (Exception e){

        }
        return selectedClub;

    }
    private Events selectedEventOptionToObject(String selectedEventOption){
        EventDataHandling obj=new EventDataHandling();
        Events selectedEvent=null;
        try {
            String[] split=selectedEventOption.split("-");
            selectedEvent=obj.loadEventData(split[0]);
        }catch (Exception e){

        }
        return selectedEvent;
    }


    private void setUpClubActivityReport(String clubIdName){
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        eventVenueColumn.setCellValueFactory(new PropertyValueFactory<>("eventLocation"));
        eventDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));

        Clubs clubs=selectedClubOptionToClubObject(clubIdName);

        EventDataHandling obj=new EventDataHandling();
        obj.loadEventDataOfAClub(clubs);

        ObservableList<Events> eventNavigateTableData=FXCollections.observableArrayList();
        try{eventNavigateTableData.addAll(clubs.getClubEvents());}catch (Exception e){
            showInfoAlert("There aren't any events under this club");
        }
        eventTable.setItems(eventNavigateTableData);
    }

}
