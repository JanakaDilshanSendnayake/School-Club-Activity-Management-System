package SceneControlersClubAdvisors;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Main;
import stake_holders.Clubs;
import utils.ClubDataHandling;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CAClubs implements Initializable {

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
    @FXML
    private TextField newClubNameField;
    @FXML
    private TextArea newClubDescriptionField;
    @FXML
    private ComboBox newClubTypeComboBox;
    @FXML
    private Label newClubIdLabel;
    @FXML
    private Label newClubNameLabel;
    @FXML
    private Label newClubDescriptionLabel;
    private ChangeListener<String> newClubIdFieldListener;
    private ChangeListener<String> newClubNameFieldListener;
    private ChangeListener<String> newClubDescriptionFieldListener;


    private static final String CLUB_NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String CLUB_DESCRIPTION_REGEX = "^.{1,200}$";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatus=false;
        caNaviClubs.toFront();


        caClubsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (updateStatus && newTab == createNewClubTab) {
                if(showConfirmationAlert("There's an ongoing update. Do you want to cancel it?")){
                    caClubsTabPane.getSelectionModel().select(newTab);
                }else{
                    caClubsTabPane.getSelectionModel().select(oldTab);
                }

            }
        });

//        // Initializing Event listener for Club Advisor ID
//        newClubIdFieldListener=((observable, oldValue, newValue) -> {
//            String validationMessage = validateTextField(newValue, USER_ID_REGEX,
//                    "User ID should be a combination letters and numbers.",10);
//            newClubIdLabel.setText(validationMessage);
//            setLabelStyle(validationMessage, newUserIdLabel);
//        });

        // Initializing Event listener for Club Advisor First Name
        newClubNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, CLUB_NAME_REGEX,
                    "Name can only contain letters and Underscores.",30);
            newClubNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newClubNameLabel);
        });

        // Initializing Event listener for Club Advisor First Name
        newClubDescriptionFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, CLUB_DESCRIPTION_REGEX,
                    "Name can only contain letters and Underscores.",150);
            newClubDescriptionLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newClubDescriptionLabel);
        });
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
    private void clickSidebar(ActionEvent actionEvent) throws IOException {

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
    @FXML
    private void handleNewClubNameChange() {
        newClubNameField.textProperty().addListener(newClubNameFieldListener);
    }
    @FXML
    private void handleNewClubDescriptionChange(){
        newClubDescriptionField.textProperty().addListener(newClubDescriptionFieldListener);
    }

    @FXML
    private void createNewClub(){
        String newClubname=newClubNameField.getText();
        String newClubType= newClubTypeComboBox.getTypeSelector();
        String newClubdescription=newClubDescriptionField.getText();

        try{
            Clubs newClub=new Clubs(newClubname,newClubType,newClubdescription,Main.currentUser);
            ClubDataHandling object=new ClubDataHandling();
            object.saveNewClubToDatabase(newClub);
        }catch (IllegalArgumentException e){
            showErrorAlert(e.toString());
        }
    }

//    When user selects a club from the table in club navigation page and press view button,
//    data of that club will be loaded in the view club details page
    @FXML
    private void viewClubDetails(){
        caViewClubs.toFront();
        //Rest of the function
    }
//  When the user want to edit the current details of the club and click edit button,
//  current data should be loaded in the editable text fields in update club details

    private boolean updateStatus; // this variable is used to check whether if there is an ongoing detail update.
    @FXML
    private void editClubDetails(){
        updateStatus=true;
        caUpdateClubs.toFront();
        //Rest of the function
    }
//  When user click suspend button the club and all the relevant information should be deleted
    @FXML
    private void suspendClub(){
        if (showConfirmationAlert("Are you sure you want to delete this club and all of the relevant details")){
            //Implement the rest of the function
            showInfoAlert("Club and all of the relevant details Succesfully deleted.");
        }else{}
    }
    @FXML
    private void leaveClub(){

        if (showConfirmationAlert("Are you sure that you want to leave the club?")){
            //Implement the rest of the function
            showInfoAlert("You succesfully left the club.");
        }else{}
    }
//  When user inputs updated data and press update button, data should be validated and saved
    @FXML
    private void updateClubDetails(){

        //Rest of the function
        updateStatus=false;
        showInfoAlert("Club details updated succesfully");
        caViewClubs.toFront();
    }
    @FXML
    private void updateCancel(){

        // Rest of the function
        updateStatus=false;
        showErrorAlert("Update cancelled");
        caViewClubs.toFront();
    }


}
