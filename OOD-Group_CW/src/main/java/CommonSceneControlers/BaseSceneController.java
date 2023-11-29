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

import java.util.Optional;
//BY JANAKA DILSHAN SENDANAYAKE RGU ID:2237952=============================================================

public class BaseSceneController {

    //REGEX-These will be used throughout the project to validate user inputs
    public static final String USER_ID_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{1,10}$";
    public static final String FIRST_NAME_REGEX = "^[a-zA-Z]{1,10}$";
    //Inputs for last name should be always letters. And there's a character limit of 20
    public static final String LAST_NAME_REGEX = "^[a-zA-Z]{1,20}$";
    //Inputs for email should end with "@iit.ac.lk". Example- janaka@iit.ac.lk
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@iit\\.ac\\.lk$";
    //Inputs for mobile number should follow ten digit format. Ex- 0712304501
    public static final String MOBILE_NUMBER_REGEX = "^\\d{10}$";
    //Inputs for password should contain at least one letter, number and special character. And the inputs should be at-
    //-least 8 characters long
    public static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";

    public Stage stage;
    public Scene scene;
    public Parent root;
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
    public void signOut(ActionEvent actionEvent){
        if(showConfirmationAlert("Are you sure that you want to sign out from the system?")){
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml_files/Common/LoginOrRegister.fxml"));
                scene = new Scene(root);
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
            }
        }
    }
    //=========================================================================================
    public void loadStudentHome(ActionEvent actionEvent){
        try{root = FXMLLoader.load(getClass().getResource("/fxml_files/Student/Menu-Students.fxml"));
            scene = new Scene(root);
            stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();}catch (Exception e){}

    }
    public void loadStudentClubs(ActionEvent actionEvent){
        try{root = FXMLLoader.load(getClass().getResource("/fxml_files/Student/Clubs-Students.fxml"));
            scene = new Scene(root);
            stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();}catch (Exception e){}

    }
    public void loadStudentEvents(ActionEvent actionEvent){
        try{root = FXMLLoader.load(getClass().getResource("/fxml_files/Student/Events-Students.fxml"));
            scene = new Scene(root);
            stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();}catch (Exception e){}

    }
    public void loadStudentAccounts(ActionEvent actionEvent){
        try{root = FXMLLoader.load(getClass().getResource("/fxml_files/Student/Account-Students.fxml"));
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
