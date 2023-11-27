package SceneControlersClubAdvisors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import utils.MySqlConnect;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class CAReport implements Initializable {
    MySqlConnect ConnectNow = new MySqlConnect();
    Connection ConnectDB = ConnectNow.getDatabaseLink();

    @FXML
    private TableColumn<EventMarking, Boolean> Attendencecol;

    @FXML
    private ComboBox<String> clubSelection;

    @FXML
    private ComboBox<String> eventSelection;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private TableColumn<EventMarking, String> stuIDcol;

    @FXML
    private TableColumn<EventMarking, String> stuNamecol;

    @FXML
    private TableView<EventMarking> tableview;
    EventMarking eventMarking = new EventMarking();

    @FXML
    void onClubChange(ActionEvent event)  {
        //get events for that club
        String club = clubSelection.getValue();
        ObservableList<String> eventNames = eventMarking.getEventNames(ConnectDB,club);
        /*String club = clubSelection.getValue();
        eventSelection.getItems().clear();
        try {
            PreparedStatement ps = ConnectDB.prepareStatement("select event_name\n" +
                    "from Event\n" +
                    "inner join Club\n" +
                    "on Event.club_id = Club.club_id\n" +
                    "where club_name = \"club_name\";");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventNames.add(rs.getString("evetn_name"));
                //eventSelection.getItems().addAll(rs.getString("event_name"));
            }
        }catch (Exception e){e.printStackTrace();}*/
        eventSelection.setItems(eventNames);
    }

    @FXML
    void onEventClick(ActionEvent event) {
// get attendence table for that event
        String clubName = clubSelection.getValue();
        String eventName = eventSelection.getValue();
        ObservableList<EventMarking> attendanceRegister = eventMarking.getRegister(ConnectDB,clubName,eventName);
        /*ObservableList<EventMarking> NameAttendence = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = ConnectDB.prepareStatement("select student.student_id, student_name, Attendance_status\n" +
                    "from attendance\n" +
                    "inner join student\n" +
                    "on attendance.student_id = student.student_id\n" +
                    "inner join event\n" +
                    "on event.event_id = attendance.event_id\n" +
                    "inner join club\n" +
                    "on club.club_id = event.club_id\n" +
                    "where club_name = \"club_name\" and event_name = \"event_name\";");// + (club) + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                //NameAttendence.add(new EventMarking(rs.getString(),rs.getString(),rs.getBoolean()));
            }*/
            stuIDcol.setCellValueFactory(new PropertyValueFactory<EventMarking,String>("StudentID"));
            stuNamecol.setCellValueFactory(new PropertyValueFactory<EventMarking,String>("StudentName"));
            Attendencecol.setCellValueFactory(new PropertyValueFactory<EventMarking, Boolean>("Attendence"));
            tableview.setItems(attendanceRegister);
        }//catch (Exception e){e.printStackTrace();}


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> clubNames = eventMarking.getClubNames(ConnectDB);
        /*ObservableList<String> clubNames = FXCollections.observableArrayList();
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/scams", "root", "#Wolf8me");
            PreparedStatement ps = ConnectDB.prepareStatement("select club_name from Club;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                //clubSelection.getItems().addAll(rs.getString("club_name"));
                clubNames.add(rs.getString("club_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        clubSelection.setItems(clubNames);
    }
}
