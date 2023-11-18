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

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CAEvents implements Initializable {


    @FXML
    private TableColumn<NameAttendence, CheckBox> Attendencecol;

    @FXML
    private ComboBox<String> ClubSelectionChoiceBox;

    @FXML
    private ComboBox<String> EventSelectionChoiceBox;

    @FXML
    private TableColumn<NameAttendence, String> StudentIDcol;

    @FXML
    private TableColumn<NameAttendence, String> SturdentNamecol;

    @FXML
    private TableView<NameAttendence> Tableview;

    @FXML
    private AnchorPane sidebar;
    public Connection databaseLink;
    ObservableList<NameAttendence> events;

    @FXML
    void EditTableview(ActionEvent event)  {
// start from here: he chooses club ---> display events for that club ----> update tableview
        String club = ClubSelectionChoiceBox.getValue();
        EventSelectionChoiceBox.getItems().clear();
         events = FXCollections.observableArrayList();
        List<CheckBox> checkBoxes = new ArrayList<>();
        try {
            PreparedStatement ps = databaseLink.prepareStatement("Select event_name from events where club_name='" + (club) + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EventSelectionChoiceBox.getItems().addAll(rs.getString("event_name"));
            }

            PreparedStatement ps1 = databaseLink.prepareStatement("select userID,name1 from club_names where club_name='"+(club)+"'");
            ResultSet rs1 = ps1.executeQuery();
            while(rs1.next()){
                CheckBox test = new CheckBox();
                events.add(new NameAttendence(rs1.getString("userID"),rs1.getString("name1"),test));
                //checkBoxes.add(test);
            }
        }catch (Exception e){
            e.printStackTrace();}


        StudentIDcol.setCellValueFactory(new PropertyValueFactory<NameAttendence,String>("StudentID"));
        SturdentNamecol.setCellValueFactory(new PropertyValueFactory<NameAttendence,String>("StudentName"));
        Attendencecol.setCellValueFactory(new PropertyValueFactory<NameAttendence,CheckBox>("checkBox"));
        Tableview.setItems(events);
    }

    @FXML
    void handleSave(ActionEvent event) throws SQLException {
        String clubname = ClubSelectionChoiceBox.getValue();
        String eventname = EventSelectionChoiceBox.getValue();

        PreparedStatement ps = databaseLink.prepareStatement("delete from event_names where event_name='"+(eventname)+"'");
        ps.executeUpdate();

        List<String> presentStudents = new ArrayList<>();
        for (NameAttendence student:events) {
            if(student.getCheckBox().isSelected()){
                PreparedStatement ps1 = databaseLink.prepareStatement("insert into event_names ( club_name,event_name,name1, studentID) values ('"+(clubname)+"','"+(eventname)+"','"+(student.getStudentName())+"','"+(student.getStudentID())+"');");
                ps1.executeUpdate();

                //presentStudents.add(student.getStudentName()+" "+student.getStudentID());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/scams", "root", "#Wolf8me");
            PreparedStatement ps = databaseLink.prepareStatement("Select club_name from clubs");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
            ClubSelectionChoiceBox.getItems().addAll(rs.getString("club_name"));}
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}