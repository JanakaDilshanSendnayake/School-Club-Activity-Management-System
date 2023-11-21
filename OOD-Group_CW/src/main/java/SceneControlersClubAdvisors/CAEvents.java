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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Main;
import stake_holders.Clubs;


import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CAEvents implements Initializable {

    //Buttons - SideBar
    @FXML private Button caHomeButton;
    @FXML private Button caClubsButton;
    @FXML private Button caEventsButton;
    @FXML private Button caReportsButton;
    @FXML private Button caAccountButton;

    //Buttons
    @FXML private Button viewButton;
    @FXML private Button updateEventButton;
    @FXML private Button updateEventCancelButton;
    @FXML private Button editEventButton;
    @FXML private Button suspendEventButton;
    //@FXML private Button leaveClubButton;
    @FXML private Button backToClubNaviButton;


    //Panes
    @FXML private TabPane caEventsTabPane;
    @FXML private Tab eventNaviTab;
    @FXML private Tab createNewEventTab;
    @FXML private AnchorPane caNaviEvents;
    @FXML private AnchorPane caViewEvents;
    @FXML private AnchorPane caUpdateEvents;
    @FXML private AnchorPane caCreateNewEvents;

    //====
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatus=false;
        caNaviEvents.toFront();


        caEventsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (updateStatus && newTab == createNewEventTab) {
                if(showConfirmationAlert("There's an ongoing update. Do you want to cancel it?")){
                    caEventsTabPane.getSelectionModel().select(newTab);
                }else{
                    caEventsTabPane.getSelectionModel().select(oldTab);
                }

            }
        });
        // initializing the event listener to validate the user input in "newClubNameField" text-field
        newClubFieldListener=((observable, oldValue, newValue) -> {
            String message=isClubNameValid(newValue, Main.clubs);
            if(Objects.equals(message,"set to the old value")){
                newClubNameField.setText(oldValue);
            }else{
                newClubLabel.setText(message);
                if (Objects.equals(message,"Valid")){
                    newClubLabel.setTextFill(Color.GREEN);
                }else{newClubLabel.setTextFill(Color.RED);}
            }

        });
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
    public void clickSidebar(ActionEvent actionEvent) throws IOException {

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
    //    When user selects a event from the table in event navigation page and press view button,
//    data of that event will be loaded in the view event details page
    @FXML
    public void viewClubDetails(){
        caViewEvents.toFront();
        //Rest of the function
    }
//  When the user want to edit the current details of the event and click edit button,
//  current data should be loaded in the editable text fields in update edit details

    private boolean updateStatus; // this variable is used to check whether if there is an ongoing detail update.
    @FXML
    public void editEventDetails(){
        updateStatus=true;
        caUpdateEvents.toFront();
        //Rest of the function
    }
    //  When user click suspend button the club and all the relevant information should be deleted
    @FXML
    public void suspendEvent(){
        if (showConfirmationAlert("Are you sure you want to suspend event.")){
            //Implement the rest of the function
            showInfoAlert("Event and all of the relevant details Successfully deleted.");
        }else{}
    }

    //  When user inputs updated data and press update button, data should be validated and saved
    @FXML
    public void updateEventDetails(){

        //Rest of the function
        updateStatus=false;
        showInfoAlert("Event details updated successfully");
        caViewEvents.toFront();
    }
    @FXML
    public void updateCancel(){

        // Rest of the function
        updateStatus=false;
        showErrorAlert("Update cancelled");
        caViewEvents.toFront();
    }

    //To validate the data inputs in Create new club tab
    @FXML
    private TextField newClubNameField;
    @FXML
    private Label newClubLabel;
    @FXML
    private ComboBox<String> newClubTypeComboBox;
    @FXML
    private Label newClubTypeLabel;
    @FXML
    private TextArea newClubDescriptionTextArea;
    @FXML
    private Label newClubDescriptionLabel;

    private ChangeListener<String> newClubFieldListener;
    private ChangeListener<String> newClubTypeListener;
    private ChangeListener<String> newClubDescriptionListener;

    public String isClubNameValid(String newValue, ArrayList<Clubs> obj){
        if (!newValue.matches("\\D*")) { // Only allow non-digit characters
            return "Only strings are allowed here";
        } else if (newValue.trim().isEmpty()) {
            return "This field is mandatory";
        } else if (newValue.trim().length() > 30) {
            return "set to the old value";
        } else {
            boolean nameExists = false;
            for (Clubs object : obj) {
                if (Objects.equals(object.getClubName().toLowerCase(), newValue.trim().toLowerCase())) {
                    nameExists = true;
                    break;
                }
            }
            if (nameExists) {
                return "This name is already taken";

            } else {
                return "Valid";
            }
        }
    }




}

