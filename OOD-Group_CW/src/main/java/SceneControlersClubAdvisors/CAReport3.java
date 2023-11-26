package SceneControlersClubAdvisors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;
import stake_holders.ClubAdvisor;
import stake_holders.Clubs;
import stake_holders.Student;
import utils.ClubDataHandling;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CAReport3 implements Initializable {

    @FXML
    private TableView<Student> studentMembersReportTable;
    @FXML private TableColumn<Student,String> studentIdColumn;
    @FXML private TableColumn<Student,String> studentNameColumn;
    @FXML private TableColumn<Student,String> studentEmailColumn;
    @FXML private TableColumn<Student,String> studentMobileNumberColumn;

    @FXML private ComboBox<String> clubsSorter;
    ObservableList<String> clubsSorterData=FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ClubDataHandling object=new ClubDataHandling();
        object.loadClubDataRelevantToCA(Main.currentUser);
        System.out.println(Main.currentUser.getClubsWithAdminAccess());

        ArrayList<String> clubNames = new ArrayList<>();

        for (Clubs ca : Main.currentUser.getClubsWithAdminAccess()) {
            clubNames.add(ca.getClubId()+"-"+ca.getClubName());
        }

        clubsSorterData.addAll(clubNames);
        clubsSorter.setItems(clubsSorterData);

        clubsSorter.setOnAction(event->{
            String selectedOption = clubsSorter.getSelectionModel().getSelectedItem();
            setUpClubAdvisorMembersNaviTable(selectedOption);
        });


    }

    private void setUpClubAdvisorMembersNaviTable( String idAndName){
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentMobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));

        String[] split=idAndName.split("-");

        ClubDataHandling object=new ClubDataHandling();
        Clubs club=object.loadClubData(split[0]);
        object.loadClubStudentMembershipData(club);

        ObservableList<Student> studentMembersToDisplay= FXCollections.observableArrayList();

        try{studentMembersToDisplay.addAll(club.getStudentMembers());}catch (Exception e){
            System.out.println(e.getMessage());
        }
        studentMembersReportTable.setItems(studentMembersToDisplay);

    }
}
