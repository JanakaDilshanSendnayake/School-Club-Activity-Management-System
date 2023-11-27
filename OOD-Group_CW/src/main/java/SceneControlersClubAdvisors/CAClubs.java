package SceneControlersClubAdvisors;

import CommonSceneControlers.BaseSceneController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;
import stake_holders.ClubAdvisor;
import stake_holders.Clubs;
import utils.ClubDataHandling;


import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Random;

public class CAClubs extends BaseSceneController implements Initializable {

    //Buttons - SideBar
    @FXML private Button caHomeButton;
    @FXML private Button caClubsButton;
    @FXML private Button caEventsButton;
    @FXML private Button caReportsButton;
    @FXML private Button caAccountButton;

    //Buttons
    @FXML private Button viewButton;
    @FXML private Button updateClubButton;
    @FXML private Button updateClubCancelButton;
    @FXML private Button editClubButton;
    @FXML private Button suspendClubButton;
    @FXML private Button leaveClubButton;
    @FXML private Button joinClubButton;
    @FXML private Button backToClubNaviButton;


    //Panes
    @FXML private TabPane caClubsTabPane;
    @FXML private Tab clubNaviTab;
    @FXML private Tab createNewClubTab;
    @FXML private AnchorPane caNaviClubs;
    @FXML private AnchorPane caViewClubs;
    @FXML private AnchorPane caUpdateClubs;
    @FXML private AnchorPane caCreateNewClubs;

    //====
    private Stage stage;
    private Scene scene;
    private Parent root;

    //Text fields
    @FXML private TextField newClubNameField;
    @FXML private TextArea newClubDescriptionField;
    @FXML private ComboBox<String> newClubTypeComboBox=new ComboBox<>();
    ObservableList<String> newClubTypeComboBoxData = FXCollections.observableArrayList(
            "Sports",
            "Astronomy",
            "Welfare",
            "Entertainment",
            "Art",
            "Chess",
            "Science",
            "Mathematics",
            "Literary",
            "Music",
            "Drama",
            "Debate",
            "Robotics",
            "Environment",
            "History",
            "Coding",
            "Language",
            "Photography"
    );

    @FXML private Label newClubNameLabel;
    @FXML private Label newClubDescriptionLabel;

    //Event listeners to monitor user inputs realtime and show messages about the inputs realtime
    private ChangeListener<String> newClubNameFieldListener;
    private ChangeListener<String> newClubDescriptionFieldListener;
    private ChangeListener<String> updateClubNameFieldListener;
    private ChangeListener<String> updateClubDescriptionFieldListener;

    //Regular expressions to validate user inputs

    private static final String CLUB_NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String CLUB_DESCRIPTION_REGEX = "^(?s).{1,200}$";

    //Tables in club navigator pane
    @FXML private TextField clubNavigateSearchbar;
    @FXML private ComboBox<String> clubNavigateClubsSorter;
    ObservableList<String> clubNavigateClubsSorterData = FXCollections.observableArrayList(
            "All Clubs",
            "My Clubs With Admin Privileges",
            "My Clubs Without Admin Privileges"
    );
    @FXML private TableView<Clubs> clubNavigateTable;
    @FXML private TableColumn<Clubs,String> clubIdColumn;
    @FXML private TableColumn<Clubs,String> clubNameColumn;
    @FXML private TableColumn<Clubs,String> clubTypeColumn;
    ObservableList<Clubs> clubsToDisplayInNaviTable=FXCollections.observableArrayList();

    //Textfields in view club details page
    @FXML private Label viewClubNameLabel;
    @FXML private TextArea viewClubDescriptionTextField;
    @FXML private Label viewClubTypeLabel;
    //Textfileds in edit club details page
    @FXML private TextField updateClubNameField;
    @FXML private ComboBox<String> updateClubTypeComboBox;
    @FXML private TextArea updateClubDescriptionField;

    //To track if the user has selected an item from the table
    private boolean isItemSelectedFromTable;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatus=false;
        caNaviClubs.toFront();
        newClubTypeComboBox.setItems(newClubTypeComboBoxData);
        clubNavigateClubsSorter.setItems(clubNavigateClubsSorterData);
        //clubNavigateClubsSorter.setValue("All Clubs");
        newClubDescriptionField.setWrapText(true);
        viewClubDescriptionTextField.setWrapText(true);
        viewClubDescriptionTextField.setEditable(false);
        isItemSelectedFromTable=false;

        caClubsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (updateStatus && newTab == createNewClubTab) {
                if(showConfirmationAlert("There's an ongoing update. Do you want to cancel it?")){
                    caClubsTabPane.getSelectionModel().select(newTab);
                }else{
                    caClubsTabPane.getSelectionModel().select(oldTab);
                }

            }
        });
        /*To handle the combo box in club navigation page
        This combo box has 3 options-"All Clubs","My Clubs With Admin Privileges" and "My Clubs Without Admin Privileges"
        When the user selects "All Clubs", all the clubs registered in the system are displayed in the clubsNavigateTable
        When user selects "My Clubs With Admin Privileges", table shows the clubs user is both a member and an admin.
        When user selects "My Clubs Without Admin Privileges", table shows the clubs user is only a member but not an admin.
        */
        clubNavigateClubsSorter.setOnAction(event->{
            ClubDataHandling object=new ClubDataHandling();
            object.loadClubDataRelevantToCA(Main.currentUser);

            String selectedOption = clubNavigateClubsSorter.getSelectionModel().getSelectedItem();

            if (selectedOption.equals("All Clubs")){
                try {
                    clubsToDisplayInNaviTable.clear();
                    setUpClubNaviTable(clubsToDisplayInNaviTable, object.loadAllClubs());
                } catch (SQLException e) {
                    showErrorAlert(e.getMessage());
                }
            } else if (selectedOption.equals("My Clubs With Admin Privileges")) {
                clubsToDisplayInNaviTable.clear();
                setUpClubNaviTable(clubsToDisplayInNaviTable,Main.currentUser.getClubsWithAdminAccess());
            } else if (selectedOption.equals("My Clubs Without Admin Privileges")) {
                clubsToDisplayInNaviTable.clear();
                setUpClubNaviTable(clubsToDisplayInNaviTable,Main.currentUser.getClubsWithoutAdminAccess());


            }
        });

        //passing the values of the selected row to text fields in the update pane
        clubNavigateTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                isItemSelectedFromTable=true;
                //System.out.println(newSelection.getClubAdvisorMembers().);
                viewClubNameLabel.setText(newSelection.getClubName());
                viewClubDescriptionTextField.setText(newSelection.getClubDescription());
                viewClubTypeLabel.setText(newSelection.getClubType());
                //========================================
                ClubDataHandling obj=new ClubDataHandling();
                obj.loadClubMembershipData(newSelection);
                obj.loadClubDataRelevantToCA(Main.currentUser);
                Main.currentClub=newSelection;


                ArrayList<String> clubsWithAdminAccessClubIds=new ArrayList<>();
                ArrayList<String> clubsWithoutAdminAccessClubIds=new ArrayList<>();

                for(Clubs club:Main.currentUser.getClubsWithAdminAccess()){
                    clubsWithAdminAccessClubIds.add(club.getClubId());
                }
                for(Clubs club:Main.currentUser.getClubsWithoutAdminAccess()){
                    clubsWithoutAdminAccessClubIds.add(club.getClubId());
                }
                //=====================================================

                if(clubsWithAdminAccessClubIds.contains(newSelection.getClubId())){
                    editClubButton.setVisible(true);
                    suspendClubButton.setVisible(true);
                    joinClubButton.setVisible(false);
                    leaveClubButton.setVisible(true);
                    appointNewAdminButton.setVisible(true);
                } else if (clubsWithoutAdminAccessClubIds.contains(newSelection.getClubId())) {
                    editClubButton.setVisible(false);
                    suspendClubButton.setVisible(false);
                    joinClubButton.setVisible(false);
                    leaveClubButton.setVisible(true);
                    appointNewAdminButton.setVisible(false);
                }else{
                    editClubButton.setVisible(false);
                    suspendClubButton.setVisible(false);
                    joinClubButton.setVisible(true);
                    leaveClubButton.setVisible(false);
                    appointNewAdminButton.setVisible(false);
                }
            }
        });

        //Initializing event listeners for input fields in new club creation page================================

        // Initializing Event listener for new club name field
        newClubNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, CLUB_NAME_REGEX,
                    "Name can only contain letters and Underscores.",30);
            newClubNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newClubNameLabel);
        });

        // Initializing Event listener for new club text area
        newClubDescriptionFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, CLUB_DESCRIPTION_REGEX,
                    "",500);
            newClubDescriptionLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newClubDescriptionLabel);
        });

        //Initializing event listeners for input fields in update club creation page================================

        //===

        clubAdvisorMembersNavigateTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                newAdmin=newSelection;
            }else{
                showErrorAlert("please select a club advisor");
            }
        });


    }
    private ClubAdvisor newAdmin;

    @FXML
    private void saveNewAdmin(){
        if(!(newAdmin ==null)){
            ArrayList<String> array=new ArrayList<>();
            for(ClubAdvisor ca:Main.currentClub.getClubAdmin()){
                array.add(ca.getClubAdvisorId());
            }
            if(array.contains(newAdmin.getClubAdvisorId())){
                showErrorAlert("Already an admin");
            }else{
                ClubDataHandling obj=new ClubDataHandling();
                obj.promoteToClubAdmin(newAdmin,Main.currentClub);
                obj.loadClubMembershipData(Main.currentClub);
                showInfoAlert("successfully appointed an new admin");
            }
        }else{
            showErrorAlert("please select a club advisor-member from the table");
        }

    }

    private void setUpClubNaviTable(ObservableList<Clubs> observableList, ArrayList<Clubs> arrayList){
        clubIdColumn.setCellValueFactory(new PropertyValueFactory<>("clubId"));
        clubNameColumn.setCellValueFactory(new PropertyValueFactory<>("clubName"));
        clubTypeColumn.setCellValueFactory(new PropertyValueFactory<>("clubType"));

        observableList.addAll(arrayList);

        //Filter table according text added in searchbar-https://www.youtube.com/watch?v=FeTrcNBVWtg&t=33s
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
    private void handleNewClubNameChange() {
        newClubNameField.textProperty().addListener(newClubNameFieldListener);
    }
    @FXML
    private void handleNewClubDescriptionChange(){
        newClubDescriptionField.textProperty().addListener(newClubDescriptionFieldListener);
    }


    //A method to automatically generate clubId
    private String generateClubId() {
        String prefix = "C";
        int randomNumber;
        ClubDataHandling object=new ClubDataHandling();

        do {
            randomNumber = new Random().nextInt(1000);
        } while (object.clubIdValidation(prefix + randomNumber));

        return prefix + randomNumber;
    }
//Club Creation=========================================================================================================
    @FXML
    private void createNewClub(){
        String newClubId=generateClubId();
        String newClubName=newClubNameField.getText();
        String newClubType= newClubTypeComboBox.getSelectionModel().getSelectedItem();
        String newClubDescription=newClubDescriptionField.getText();

        // The user inputs will be validated inside the Clubs class constructor. If inputs are invalid, an
        // IllegalArgumentException will be thrown.
        try{
            Clubs newClub=new Clubs(newClubId, newClubName,newClubType,newClubDescription,Main.currentUser);
            //System.out.println(newClub.getClubAdmin().getName());
            //Adding the created club to current user
            Main.currentUser.getClubsWithAdminAccess().add(newClub);//-------------------------
            //saving the created club to the club table and club-advisor_club table
            ClubDataHandling object=new ClubDataHandling();
            object.saveNewClubToDatabase(newClub);
            //Showing user the success alert.
            showInfoAlert(newClubName+": Successfully created!.");

            //Removing the current inputs from the text fields.
            clearUserInputsFieldsInClubCreationPage();
        }catch (IllegalArgumentException e){
            showErrorAlert(e.toString());
        }
    }
    @FXML
    private void clearUserInputsFieldsInClubCreationPage(){
        newClubNameField.textProperty().removeListener(newClubNameFieldListener);
        newClubDescriptionField.textProperty().removeListener(newClubDescriptionFieldListener);
        newClubNameField.setText("");
        newClubDescriptionField.setText("");
        newClubTypeComboBox.setValue(null);
        newClubNameLabel.setText("");
        newClubDescriptionLabel.setText("");
        newClubDescriptionLabel.setText("");
    }
//Club Navigation and Updating==========================================================================================

//    When user selects a club from the table in club navigation page and press view button,
//    data of that club will be loaded in the view club details page
    @FXML
    private void viewClubDetails(){
        if(isItemSelectedFromTable){
            caViewClubs.toFront();
        }else{
            showErrorAlert("Select a club from the table.");
        }

        //Rest of the function
    }
//  When the user want to edit the current details of the club and click edit button,
//  current data should be loaded in the editable text fields in update club details

    private boolean updateStatus; // this variable is used to check whether if there is an ongoing club detail update.
    @FXML
    private void updateClubDetails(){
        if(showConfirmationAlert("Are you sure that you want to update club details?")) {

//            ClubDataHandling obj = new ClubDataHandling();
//            obj.loadClubMembershipData(Main.currentClub);

            ArrayList<String> clubAdminIds = new ArrayList<>();

            for (ClubAdvisor ca : Main.currentClub.getClubAdmin()) {
                clubAdminIds.add(ca.getClubAdvisorId());
            }

            if (clubAdminIds.contains(Main.currentUser.getClubAdvisorId())) {
                updateStatus = true;
                updateClubNameField.setText(viewClubNameLabel.getText());
                updateClubTypeComboBox.setValue(viewClubTypeLabel.getText());
                updateClubDescriptionField.setText(viewClubDescriptionTextField.getText());
                caUpdateClubs.toFront();
            } else {
                showErrorAlert("You aren't authorized for this action!");
            }
        }

    }

//  When user click suspend button the club and all the relevant information should be deleted
    @FXML
    private void suspendClub(){
        if (showConfirmationAlert("Are you sure you want to delete this club and all of the relevant details")){
            //Implement the rest of the function
            showInfoAlert("Club and all of the relevant details Successfully deleted.");
        }else{}
    }
    @FXML
    private void leaveClub(){
        if (showConfirmationAlert("Are you sure that you want to leave the club?")){
            ClubDataHandling obj=new ClubDataHandling();
            obj.loadClubMembershipData(Main.currentClub);

            ArrayList<String> clubAdminIds = new ArrayList<>();

            for (ClubAdvisor ca : Main.currentClub.getClubAdmin()) {
                clubAdminIds.add(ca.getClubAdvisorId());
            }

            if(clubAdminIds.contains(Main.currentUser.getClubAdvisorId()) && Main.currentClub.getClubAdmin().size()==1){
                showErrorAlert("you can't leave the club. Please appoint a new admin before leaving");
            }else{
                ClubDataHandling object=new ClubDataHandling();
                obj.removeClubAdvisor(Main.currentUser,Main.currentClub);
                showInfoAlert("You successfully left the club.");
            }
        }
    }
    @FXML
    private void joinClub(){
        if(showConfirmationAlert("Are you sure that you want to join this club?")){
            ClubDataHandling obj=new ClubDataHandling();
            obj.addANewCAMember(Main.currentUser,Main.currentClub);
            showInfoAlert("You joined:"+Main.currentClub.getClubName()+" successfully");
        }


    }
    @FXML
    private void appointNewAdmin(){
        setUpClubAdvisorMembersNaviTable(Main.currentClub);
        adminAppointPane.toFront();

    }

    @FXML private TableView<ClubAdvisor> clubAdvisorMembersNavigateTable;
    @FXML private TableColumn<ClubAdvisor,String> clubAdvisorMembersIdColumn;
    @FXML private TableColumn<ClubAdvisor,String> clubAdvisorMembersNameColumn;
    @FXML private AnchorPane adminAppointPane;
    @FXML private Button appointNewAdminButton;

    private void setUpClubAdvisorMembersNaviTable( Clubs club){
        clubAdvisorMembersIdColumn.setCellValueFactory(new PropertyValueFactory<>("clubAdvisorId"));
        clubAdvisorMembersNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        ObservableList<ClubAdvisor> clubsAdvisorMembersToDisplay=FXCollections.observableArrayList();

        try{clubsAdvisorMembersToDisplay.addAll(club.getClubAdvisorMembers());}catch (Exception e){
            System.out.println(e.getMessage());
        }
        clubsAdvisorMembersToDisplay.addAll(club.getClubAdmin());
        System.out.println(club.getClubAdmin());
        clubAdvisorMembersNavigateTable.setItems(clubsAdvisorMembersToDisplay);

        //Javafx: TableView change row color based on column value-https://stackoverflow.com/a/56309916
        clubAdvisorMembersNavigateTable.setRowFactory(tv -> new TableRow<ClubAdvisor>() {
            @Override
            protected void updateItem(ClubAdvisor item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle(""); // Reset style for empty rows
                } else if (club.getClubAdmin().contains(item)) {
                    // Apply a different color to rows containing objects from club.getClubAdmin()
                    setStyle("-fx-background-color: lightblue;");
                } else {
                    setStyle(""); // Reset style for other rows
                }
            }
        });
    }





    //===================================================================
//  When user inputs updated data and press update button, data should be validated and saved
    @FXML
    private void saveUpdatedClubDetails(){
        String clubId=Main.currentClub.getClubId();
        String updatedClubName=updateClubNameField.getText();
        String updatedClubType = updateClubTypeComboBox.getSelectionModel().getSelectedItem();
        String updatedClubDescription=updateClubDescriptionField.getText();

        Clubs clubObjectWithUpdatedDeatils=new Clubs(clubId,updatedClubName,updatedClubType,updatedClubDescription);
        ClubDataHandling object=new ClubDataHandling();
        object.updateClubInDatabase(clubObjectWithUpdatedDeatils);

        //Main.currentClub=clubObjectWithUpdatedDetails;
        //Rest of the function
        updateStatus=false;
        showInfoAlert("Club details updated successfully");
        caNaviClubs.toFront();
    }
    @FXML
    private void cancelUpdate(){

        // Rest of the function
        updateStatus=false;
        showErrorAlert("Update cancelled");
        caViewClubs.toFront();

    }
    @FXML
    private void goBackToClubNavigation(){
        caNaviClubs.toFront();
    }

    @FXML
    private void fromAdminPageToClubDescription(){
        caViewClubs.toFront();
    }


}
