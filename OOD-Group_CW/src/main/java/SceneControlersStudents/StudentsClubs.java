package SceneControlersStudents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.Main;
import stake_holders.Clubs;
import utils.ClubDataHandling;
import CommonSceneControlers.BaseSceneController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 *
 *BY AKSHITH RAJENDRAN RGU ID: 2236761
 *
 */

public class StudentsClubs extends BaseSceneController implements Initializable  {

    //Club navigate pane javafx elements======================================
    @FXML private AnchorPane naviClubsPane;
    @FXML private TextField clubNavigateSearchbar;
    @FXML private ComboBox<String> clubNavigateSorter;
    ObservableList<String> clubNavigateSorterData = FXCollections.observableArrayList(
            "All Clubs",
            "My Clubs"
    );
    @FXML
    private TableView<Clubs> clubNavigateTable;
    @FXML private TableColumn<Clubs,String> clubIdColumn;
    @FXML private TableColumn<Clubs,String> clubNameColumn;
    @FXML private TableColumn<Clubs,String> clubTypeColumn;
    ObservableList<Clubs> clubsToDisplayInNaviTable= FXCollections.observableArrayList();

    //To track if the user has selected an item from the table
    private boolean isItemSelectedFromTable;
    //view club details pane==========================================================================
    @FXML private AnchorPane viewClubPane;
    @FXML private Label viewClubNameLabel;
    @FXML private TextArea viewClubDescriptionTextField;
    @FXML private Label viewClubTypeLabel;

    @FXML private Button leaveClubButton;
    @FXML private Button joinClubButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        naviClubsPane.toFront();
        clubNavigateSorter.setItems(clubNavigateSorterData);
        viewClubDescriptionTextField.setWrapText(true);



        clubNavigateSorter.setOnAction(event->{
            ClubDataHandling object=new ClubDataHandling();
            object.loadClubDataRelevantToStudent(Main.currentStudentUser);

            String selectedOption = clubNavigateSorter.getSelectionModel().getSelectedItem();

            if (selectedOption.equals("All Clubs")){
                try {
                    clubsToDisplayInNaviTable.clear();
                    setUpClubNaviTable(clubsToDisplayInNaviTable, object.loadAllClubs());
                } catch (SQLException e) {
                    showErrorAlert(e.getMessage());
                }
            } else if (selectedOption.equals("My Clubs")) {
                clubsToDisplayInNaviTable.clear();
                setUpClubNaviTable(clubsToDisplayInNaviTable,Main.currentStudentUser.getJoinedClubs());
            }
        });

        clubNavigateTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                isItemSelectedFromTable=true;
                Main.currentClub=newSelection;

                ClubDataHandling obj=new ClubDataHandling();
                obj.loadClubStudentMembershipData(newSelection);
                obj.loadClubDataRelevantToStudent(Main.currentStudentUser);

                viewClubNameLabel.setText(newSelection.getClubName());
                viewClubDescriptionTextField.setText(newSelection.getClubDescription());
                viewClubTypeLabel.setText(newSelection.getClubType());

                ArrayList<String> clubsStudentHaveJoinedClubIds=new ArrayList<>();
                for(Clubs club:Main.currentStudentUser.getJoinedClubs()){
                    clubsStudentHaveJoinedClubIds.add(club.getClubId());
                }if(clubsStudentHaveJoinedClubIds.contains(newSelection.getClubId())){
                    joinClubButton.setVisible(false);
                    leaveClubButton.setVisible(true);
                }else{
                    joinClubButton.setVisible(true);
                    leaveClubButton.setVisible(false);
                }

            }
        });
    }

    private void setUpClubNaviTable(ObservableList<Clubs> observableList, ArrayList<Clubs> arrayList){
        clubIdColumn.setCellValueFactory(new PropertyValueFactory<>("clubId"));
        clubNameColumn.setCellValueFactory(new PropertyValueFactory<>("clubName"));
        clubTypeColumn.setCellValueFactory(new PropertyValueFactory<>("clubType"));

        observableList.addAll(arrayList);

        FilteredList<Clubs> filteredData=new FilteredList<>(observableList, b -> true);

        clubNavigateSearchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(club -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return club.getClubName().toLowerCase().startsWith(lowerCaseFilter);
            });
        });
        //Wrap the FilteredList in a SortedList.
        SortedList<Clubs> sortedData = new SortedList<>(filteredData);
        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(clubNavigateTable.comparatorProperty());
        clubNavigateTable.setItems(sortedData);

    }
    @FXML
    private void viewClub(){
        viewClubPane.toFront();
    }
    @FXML
    private void joinClub(){
        if(showConfirmationAlert("Are you sure that you want to join this club? ")) {
            ClubDataHandling obj = new ClubDataHandling();
            obj.addANewStudentMember(Main.currentStudentUser, Main.currentClub);
            showInfoAlert("You joined the club successfully");
        }
    }

    @FXML
    private void leaveClub(){
        if(showConfirmationAlert("Are you sure that you want to leave this club? ")){
            ClubDataHandling obj=new ClubDataHandling();
            obj.removeStudentMember(Main.currentStudentUser,Main.currentClub);
            showInfoAlert("You joined the club successfully");
        }

    }
    @FXML
    private void backToStudentNaviPane(){
        naviClubsPane.toFront();
    }
}
