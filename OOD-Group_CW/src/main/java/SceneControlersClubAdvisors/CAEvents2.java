package SceneControlersClubAdvisors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import utils.MySqlConnect;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class CAEvents2 implements Initializable {

    MySqlConnect ConnectNow = new MySqlConnect();
    Connection ConnectDB = ConnectNow.getDatabaseLink();

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
    ObservableList<EventMarking> events;

    @FXML
    void EditTableview(ActionEvent event)  {
// start from here: he chooses club ---> display events for that club ----> update tableview
        String club = ClubSelectionChoiceBox.getValue();
        EventSelectionChoiceBox.getItems().clear();
        events = FXCollections.observableArrayList();
        List<CheckBox> checkBoxes = new ArrayList<>();
        try {
            PreparedStatement ps = ConnectDB.prepareStatement("select event_name\n" +
                    "from Event\n" +
                    "inner join Club\n" +
                    "on Event.club_id = Club.club_id\n" +
                    "where club_name = \"club_name\";");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EventSelectionChoiceBox.getItems().addAll(rs.getString("event_name"));
            }

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
            e.printStackTrace();}


        StudentIDcol.setCellValueFactory(new PropertyValueFactory<EventMarking,String>("StudentID"));
        SturdentNamecol.setCellValueFactory(new PropertyValueFactory<EventMarking,String>("StudentName"));
        Attendencecol.setCellValueFactory(new PropertyValueFactory<EventMarking,CheckBox>("checkBox"));
        Tableview.setItems(events);
    }

    @FXML
    void handleSave(ActionEvent event) throws SQLException {
        String clubname = ClubSelectionChoiceBox.getValue();
        String eventname = EventSelectionChoiceBox.getValue();
//get event id
        PreparedStatement statement = ConnectDB.prepareStatement("select event_id \n" +
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
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/scams", "root", "#Wolf8me");
            PreparedStatement ps = ConnectDB.prepareStatement("select club_name from Club;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
            ClubSelectionChoiceBox.getItems().addAll(rs.getString("club_name"));}
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}