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
    private TableColumn<NameAttendence, Boolean> Attendencecol;

    @FXML
    private ComboBox<String> clubSelection;

    @FXML
    private ComboBox<String> eventSelection;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private TableColumn<NameAttendence, String> stuIDcol;

    @FXML
    private TableColumn<NameAttendence, String> stuNamecol;

    @FXML
    private TableView<NameAttendence> tableview;

    @FXML
    void onClubChange(ActionEvent event)  {
        //get events for that club
        String club = clubSelection.getValue();
        eventSelection.getItems().clear();
        try {
            PreparedStatement ps = ConnectDB.prepareStatement("select event_name\n" +
                    "from Event\n" +
                    "inner join Club\n" +
                    "on Event.club_id = Club.club_id\n" +
                    "where club_name = \"club_name\";");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventSelection.getItems().addAll(rs.getString("event_name"));
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @FXML
    void onEventClick(ActionEvent event) {
// get attendence table for that event
        ObservableList<NameAttendence> NameAttendence = FXCollections.observableArrayList();
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
                //NameAttendence.add(new NameAttendence(rs.getString(),rs.getString(),rs.getBoolean()));
            }
            stuIDcol.setCellValueFactory(new PropertyValueFactory<NameAttendence,String>("studentID"));
            stuNamecol.setCellValueFactory(new PropertyValueFactory<NameAttendence,String>("studentName"));
            Attendencecol.setCellValueFactory(new PropertyValueFactory<NameAttendence, Boolean>("Attendence"));
            tableview.setItems(NameAttendence);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/scams", "root", "#Wolf8me");
            PreparedStatement ps = ConnectDB.prepareStatement("select club_name from Club;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                clubSelection.getItems().addAll(rs.getString("club_name"));}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
