package SceneControlersClubAdvisors;

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
//    When user selects a club from the table in club navigation page and press view button,
//    data of that club will be loaded in the view club details page
    @FXML
    public void viewClubDetails(){
        caViewClubs.toFront();
        //Rest of the function
    }
//  When the user want to edit the current details of the club and click edit button,
//  current data should be loaded in the editable text fileds in update club details

    private boolean updateStatus; // this variable is used to check whether if there is an ongoing detail update.
    @FXML
    public void editClubDetails(){
        updateStatus=true;
        caUpdateClubs.toFront();
        //Rest of the function
    }
//  When user click suspend button the club and all of the relevant infromation should be deleted
    @FXML
    public void suspendClub(){
        if (showConfirmationAlert("Are you sure you want to delete this club and all of the relevant details")){
            //Implement the rest of the function
            showInfoAlert("Club and all of the relevant details Succesfully deleted.");
        }else{}
    }
    @FXML
    public void leaveClub(){

        if (showConfirmationAlert("Are you sure that you want to leave the club?")){
            //Implement the rest of the function
            showInfoAlert("You succesfully left the club.");
        }else{}
    }
//  When user inputs updated data and press update button, data should be validated and saved
    @FXML
    public void updateClubDetails(){

        //Rest of the function
        updateStatus=false;
        showInfoAlert("Club details updated succesfully");
        caViewClubs.toFront();

        //Rest of the function
    }
    @FXML
    public void updateCancel(){
        updateStatus=false;
        showErrorAlert("Update cancelled");
        caViewClubs.toFront();
    }


}
