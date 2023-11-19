package SceneControlersClubAdvisors;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import stake_holders.ClubAdvisor;

import java.net.URL;
import java.util.ResourceBundle;

public class CARegistration implements Initializable {

    //REGEX
    private static final String CLUB_ADVISOR_ID_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{1,10}$";
    private static final String FIRST_NAME_REGEX = "^[a-zA-Z]{1,10}$";
    private static final String LAST_NAME_REGEX = "^[a-zA-Z]{1,20}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@iit\\.ac\\.lk$";
    private static final String MOBILE_NUMBER_REGEX = "^\\d{10}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";
    @FXML
    private AnchorPane clubAdvisorRegistrationPane;

    //Text fields to get the details from new club advisor who are joining the system
    @FXML
    private TextField newClubAdvisorIdField;
    @FXML
    private TextField newClubAdvisorFirstNameField;
    @FXML
    private TextField newClubAdvisorLastNameField;
    @FXML
    private TextField newClubAdvisorEmailField;
    @FXML
    private TextField newClubAdvisorTeleField;
    @FXML
    private TextField newClubAdvisorPasswordField1;
    @FXML
    private TextField newClubAdvisorPasswordField2;

    //Text labels to show the relevant information about the inputs they have given to above text fields in real time
    @FXML
    private Label newClubAdvisorIdLabel;
    @FXML
    private Label newClubAdvisorFirstNameLabel;
    @FXML
    private Label newClubAdvisorLastNameLabel;
    @FXML
    private Label newClubAdvisorEmailLabel;
    @FXML
    private Label newClubAdvisorTeleLabel;
    @FXML
    private Label newClubAdvisorPasswordField1Label;
    @FXML
    private Label newClubAdvisorPasswordField2Label;
    private ChangeListener<String> newClubAdvisorIdFieldListener;
    private ChangeListener<String> newClubAdvisorFirstNameFieldListener;
    private ChangeListener<String> newClubAdvisorLastNameFieldListener;
    private ChangeListener<String> newClubAdvisorEmailFieldListener;
    private ChangeListener<String> newClubAdvisorTeleFieldListener;
    private ChangeListener<String> newClubAdvisorPassword1Listener;
    private ChangeListener<String> newClubAdvisorPassword2Listener;

    //Button


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> clubAdvisorRegistrationPane.requestFocus());

        // Event listener for Club Advisor ID
        newClubAdvisorIdFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, CLUB_ADVISOR_ID_REGEX,
                    "Club Advisor ID should be a combination letters and numbers.",10);
            newClubAdvisorIdLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newClubAdvisorIdLabel);
        });

        // Event listener for Club Advisor First Name
        newClubAdvisorFirstNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, FIRST_NAME_REGEX,
                    "Name can only contain letters.",10);
            newClubAdvisorFirstNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newClubAdvisorFirstNameLabel);
        });

        // Event listener for Club Advisor Last Name
        newClubAdvisorLastNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, LAST_NAME_REGEX,
                    "Name can only contain letters.",20);
            newClubAdvisorLastNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newClubAdvisorLastNameLabel);
        });

        // Event listener for Club Advisor Email
        newClubAdvisorEmailFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue,
                    EMAIL_REGEX, "Invalid email format.");
            newClubAdvisorEmailLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newClubAdvisorEmailLabel);
        });

        // Event listener for Club Advisor Telephone
        newClubAdvisorTeleFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, MOBILE_NUMBER_REGEX,
                    "Invalid mobile number format.");
            newClubAdvisorTeleLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newClubAdvisorTeleLabel);
        });

        // Event listener for Password Field 1
        newClubAdvisorPassword1Listener=((observable, oldValue, newValue) -> {
            String validationMessage = validatePasswordField(newValue, PASSWORD_REGEX);
            newClubAdvisorPasswordField1Label.setText(validationMessage);
            setLabelStyle(validationMessage, newClubAdvisorPasswordField1Label);
            validatePasswordMatch();
        });

        // Event listener for Password Field 2
        newClubAdvisorPassword2Listener=((observable, oldValue, newValue) -> {
            validatePasswordMatch();
        });
    }

    //Method to validate string inputs
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

    private String validateTextField(String value, String regex, String invalidMessage) {
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
    private String validatePasswordField(String value,String regex) {
        if (value.matches(regex)) {
            return "Valid";
        } else {
            return "Password must be at least 8 characters long and include letters, numbers, and special characters.";
        }
    }
    //Method to check if the passwords in both fields matches
    private void validatePasswordMatch() {
        if (newClubAdvisorPasswordField1.getText().equals(newClubAdvisorPasswordField2.getText())) {
            newClubAdvisorPasswordField2Label.setText("Passwords match");
            newClubAdvisorPasswordField2Label.setStyle("-fx-text-fill: green;");
        } else {
            newClubAdvisorPasswordField2Label.setText("Passwords do not match");
            newClubAdvisorPasswordField2Label.setStyle("-fx-text-fill: red;");
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

    @FXML
    private void handleNewClubAdvisorIdChange() {
        newClubAdvisorIdField.textProperty().addListener(newClubAdvisorIdFieldListener);
    }
    @FXML
    private void handleNewClubAdvisorFirstNameChange() {
        newClubAdvisorFirstNameField.textProperty().addListener(newClubAdvisorFirstNameFieldListener);
    }
    @FXML
    private void handleNewClubAdvisorLastNameChange(){
        newClubAdvisorLastNameField.textProperty().addListener(newClubAdvisorLastNameFieldListener);
    }
    @FXML
    private void handleNewClubAdvisorEmailChange() {
        newClubAdvisorEmailField.textProperty().addListener(newClubAdvisorEmailFieldListener);
    }
    @FXML
    private void handleNewClubAdvisorTeleChange() {
        newClubAdvisorTeleField.textProperty().addListener(newClubAdvisorTeleFieldListener);
    }
    @FXML
    private void handleNewClubAdvisorPassword1Change() {
        newClubAdvisorPasswordField1.textProperty().addListener(newClubAdvisorPassword1Listener);
    }
    @FXML
    private void handleNewClubAdvisorPassword2Change() {
        newClubAdvisorPasswordField2.textProperty().addListener(newClubAdvisorPassword2Listener);
    }

    public void register(){

        try{
            String newId=newClubAdvisorIdField.getText();
            String newName= newClubAdvisorFirstNameField.getText().toLowerCase()+"_"+newClubAdvisorLastNameField.getText().toLowerCase();
            String newEmail=newClubAdvisorEmailField.getText();
            String newTele=newClubAdvisorTeleField.getText();
            String newPassword=newClubAdvisorPasswordField1.getText();
            ClubAdvisor newClubAdvisor=new ClubAdvisor(newId,newName,newEmail,newTele,newPassword);

            //Should implement a method that takes above object as an argument and save the data in relevent table fields

            //Only for testing
            System.out.println(newClubAdvisor);
            System.out.println(newClubAdvisor.getClubAdvisorId());
            System.out.println(newClubAdvisor.getName());
            System.out.println(newClubAdvisor.getEmail());
            System.out.println(newClubAdvisor.getMobileNum());
            System.out.println(newClubAdvisor.getPassword());
        }catch (Exception e){
            showErrorAlert("You have entered at least one invalid input");
        }

    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

