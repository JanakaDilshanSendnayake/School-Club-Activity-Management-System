package CommonSceneControlers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.CADataHandling;
import utils.StudentDataHandling;

import java.util.Objects;
import java.util.Optional;

public class BaseSceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    public void loadHome(ActionEvent actionEvent){
        try{root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Menu-ClubAdvisor.fxml"));
            scene = new Scene(root);
            stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();}catch (Exception e){}

    }
    public void loadClubs(ActionEvent actionEvent){
        try{root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Clubs-ClubAdvisor.fxml"));
            scene = new Scene(root);
            stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();}catch (Exception e){}

    }
    public void loadEvents(ActionEvent actionEvent){
        try{root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Events-ClubAdvisor.fxml"));
            scene = new Scene(root);
            stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();}catch (Exception e){}

    }
    public void loadReports(ActionEvent actionEvent){
        try{root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Report-ClubAdvisor.fxml"));
            scene = new Scene(root);
            stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();}catch (Exception e){}

    }
    public void loadAccounts(ActionEvent actionEvent){
        try{root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Account-ClubAdvisor.fxml"));
            scene = new Scene(root);
            stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();}catch (Exception e){}

    }
    //Methods to validate the user inputs given to textfields through out the program===================================

    //Method to validate string inputs with a character limit
    public String validateTextField(String value, String regex, String invalidMessage,int maximumCharacterLim) {
        if (value.matches(regex)) {
            return "Valid";
        } else {
            if (value.length()>maximumCharacterLim){
                return "Maximum character limit exceeded";
            }else{
                return invalidMessage;}
        }
    }
    //Overriding the above method. This method is to validate String inputs that doesn't have any character limit
    public String validateTextField(String value, String regex, String invalidMessage) {
        if (value.matches(regex)) {
            return "Valid";
        } else {
            if (value.length()>30){
                return "Maximum character limit exceeded";
            }else{
                return invalidMessage;}
        }
    }
    //Method to validate passwords
    public String validatePasswordField(String value,String regex) {
        if (value.matches(regex)) {
            return "Valid";
        } else {
            return "Password must be at least 8 characters long and include letters, numbers, and special characters.";
        }
    }

    //Method to handle the color changes of the messages that's shown the text labels
    public void setLabelStyle(String validationMessage, Label label) {
        if (validationMessage.equals("Valid")) {
            label.setStyle("-fx-text-fill: green;");
        } else {
            label.setStyle("-fx-text-fill: red;");
        }
    }

    //Pop up boxes generators===========================================================================================
    public void showInfoAlert( String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public boolean showConfirmationAlert(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }





}
