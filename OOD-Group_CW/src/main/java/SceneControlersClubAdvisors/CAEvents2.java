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
            PreparedStatement ps = ConnectDB.prepareStatement("Select event_name from events where club_name='" + (club) + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EventSelectionChoiceBox.getItems().addAll(rs.getString("event_name"));
            }

            PreparedStatement ps1 = ConnectDB.prepareStatement("select userID,name1 from club_names where club_name='"+(club)+"'");
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

        PreparedStatement ps = ConnectDB.prepareStatement("delete from event_names where event_name='"+(eventname)+"'");
        ps.executeUpdate();

        List<String> presentStudents = new ArrayList<>();
        for (EventMarking student:events) {
            if(student.getCheckBox().isSelected()){
                PreparedStatement ps1 = ConnectDB.prepareStatement("insert into event_names ( club_name,event_name,name1, studentID) values ('"+(clubname)+"','"+(eventname)+"','"+(student.getStudentName())+"','"+(student.getStudentID())+"');");
                ps1.executeUpdate();// insert bool = True

                //presentStudents.add(student.getStudentName()+" "+student.getStudentID());
            }PreparedStatement ps1 = ConnectDB.prepareStatement("insert into event_names ( club_name,event_name,name1, studentID) values ('"+(clubname)+"','"+(eventname)+"','"+(student.getStudentName())+"','"+(student.getStudentID())+"');");
            ps1.executeUpdate();// insert bool = False
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/scams", "root", "#Wolf8me");
            PreparedStatement ps = ConnectDB.prepareStatement("Select club_name from clubs");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
            ClubSelectionChoiceBox.getItems().addAll(rs.getString("club_name"));}
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}