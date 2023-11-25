package CommonSceneControlers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Main;
import stake_holders.ClubAdvisor;
import stake_holders.Student;
import utils.CADataHandling;
import utils.StudentDataHandling;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AccountManager implements Initializable {
    //Inputs for first name should be always letters. And there's a character limit of 10
    private static final String FIRST_NAME_REGEX = "^[a-zA-Z]{1,10}$";
    //Inputs for last name should be always letters. And there's a character limit of 20
    private static final String LAST_NAME_REGEX = "^[a-zA-Z]{1,20}$";
    //Inputs for email should end with "@iit.ac.lk". Example- janaka@iit.ac.lk
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@iit\\.ac\\.lk$";
    //Inputs for mobile number should follow ten digit format. Ex- 0712304501
    private static final String MOBILE_NUMBER_REGEX = "^\\d{10}$";
    //Inputs for password should contain at least one letter, number and special character. And the inputs should be at-
    //-least 8 characters long
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";

    //Text fields to get the details from new users who are joining the system
    @FXML
    private TextField userIdField;
    @FXML
    private TextField userFirstNameField;
    @FXML
    private TextField userLastNameField;
    @FXML
    private TextField userEmailField;
    @FXML
    private TextField userTeleField;
    @FXML
    private TextField userPasswordField1;
    @FXML
    private TextField userPasswordField2;

    //Text labels to show the relevant information about the inputs they have given to above text fields in real time
    @FXML
    private Label userIdLabel;
    @FXML
    private Label userFirstNameLabel;
    @FXML
    private Label userLastNameLabel;
    @FXML
    private Label userEmailLabel;
    @FXML
    private Label userTeleLabel;
    @FXML
    private Label userPasswordField1Label;
    @FXML
    private Label userPasswordField2Label;

    //event listeners to validate and show the users messages about their inputs in realtime.
    //For example if user enters a invalid input to the username field, it will show a message saying that the input is-
    //-wrong in the text label which is under the username field. these will be initialized in the initialize method and-
    //-will be later assigned to the fields
    private ChangeListener<String> userFirstNameFieldListener;
    private ChangeListener<String> userLastNameFieldListener;
    private ChangeListener<String> userEmailFieldListener;
    private ChangeListener<String> userTeleFieldListener;
    private ChangeListener<String> userPassword1Listener;
    private ChangeListener<String> userPassword2Listener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userIdField.setEditable(false);
        userFirstNameField.setEditable(false);
        userLastNameField.setEditable(false);
        userEmailField.setEditable(false);
        userPasswordField1.setEditable(false);
        userPasswordField2.setVisible(false);


        if(LoginAndRegistration.currentUserType.equals("CLUB-ADVISOR")){
            userIdField.setText(Main.currentUser.getClubAdvisorId());
            String[] fullName=Main.currentUser.getName().split("-");
            userFirstNameField.setText(fullName[0]);
            userLastNameField.setText(fullName[1]);
            userEmailField.setText(Main.currentUser.getEmail());
            userPasswordField1.setText(Main.currentUser.getPassword());
            userPasswordField2.setText(Main.currentUser.getPassword());
        } else if (LoginAndRegistration.currentUserType.equals("STUDENT")) {
            userIdField.setText(Main.currentStudentUser.getStudentId());
            String[] fullName=Main.currentStudentUser.getName().split("-");
            userFirstNameField.setText(fullName[0]);
            userLastNameField.setText(fullName[1]);
            userEmailField.setText(Main.currentStudentUser.getEmail());
            userPasswordField1.setText(Main.currentStudentUser.getPassword());
            userPasswordField2.setText(Main.currentStudentUser.getPassword());
        }


        // Initializing Event listener for Club Advisor First Name
        userFirstNameFieldListener =((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, FIRST_NAME_REGEX,
                    "Name can only contain letters.",10);
            userFirstNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, userFirstNameLabel);
        });

        // Initializing Event listener for Club Advisor Last Name
        userLastNameFieldListener =((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, LAST_NAME_REGEX,
                    "Name can only contain letters.",20);
            userLastNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, userLastNameLabel);
        });

        // Initializing Event listener for Club Advisor Email
        userEmailFieldListener =((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue,
                    EMAIL_REGEX, "Invalid email format.");
            userEmailLabel.setText(validationMessage);
            setLabelStyle(validationMessage, userEmailLabel);
        });

        // Initializing Event listener for Club Advisor Telephone
        userTeleFieldListener =((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, MOBILE_NUMBER_REGEX,
                    "Invalid mobile number format.");
            userTeleLabel.setText(validationMessage);
            setLabelStyle(validationMessage, userTeleLabel);
        });

        // Initializing Event listener for Password Field 1
        userPassword1Listener =((observable, oldValue, newValue) -> {
            String validationMessage = validatePasswordField(newValue, PASSWORD_REGEX);
            userPasswordField1Label.setText(validationMessage);
            setLabelStyle(validationMessage, userPasswordField1Label);
            validatePasswordMatch();
        });

        // Initializing Event listener for Password Field 2
        userPassword2Listener =((observable, oldValue, newValue) -> {
            validatePasswordMatch();
        });

    }

    @FXML
    private void handleNewUserFirstNameChange() {
        userFirstNameField.textProperty().addListener(userFirstNameFieldListener);
    }
    @FXML
    private void handleNewUserLastNameChange(){
        userLastNameField.textProperty().addListener(userLastNameFieldListener);
    }
    @FXML
    private void handleNewUserEmailChange() {
        userEmailField.textProperty().addListener(userEmailFieldListener);
    }
    @FXML
    private void handleNewUserTeleChange() {
        userTeleField.textProperty().addListener(userTeleFieldListener);
    }
    @FXML
    private void handleNewUserPassword1Change() {
        userPasswordField1.textProperty().addListener(userPassword1Listener);
    }
    @FXML
    private void handleNewUserPassword2Change() {
        userPasswordField2.textProperty().addListener(userPassword2Listener);
    }

    @FXML private Button updateButton;
    @FXML private Button saveButton;
    @FXML private Button cancelTheUpdateButton;
    @FXML private Button ResetThePassword;

    @FXML
    private void update(){
        handleNewUserFirstNameChange();
        handleNewUserLastNameChange();
        handleNewUserEmailChange();
        handleNewUserTeleChange();

        userFirstNameField.setEditable(true);
        userLastNameField.setEditable(true);
        userEmailField.setEditable(true);
        userPasswordField1.setEditable(true);
    }
    @FXML
    private void resetThePassword(){
        userPasswordField1.setEditable(true);
        userPasswordField2.setVisible(true);
        handleNewUserPassword1Change();
        handleNewUserPassword2Change();
    }

    @FXML
    private void saveTheUpdate(){
        String userId=userIdField.getText();
        String newName= userFirstNameField.getText().toLowerCase()+"_"+userLastNameField.getText().toLowerCase();
        String newEmail=userEmailField.getText();
        String newTele=userTeleField.getText();
        String newPassword;
        if(validatePasswordMatch()){newPassword=userPasswordField1.getText();}else{newPassword="invalid";}
        try{
            if(LoginAndRegistration.currentUserType.equals("CLUBADVISOR")){
                ClubAdvisor updatedUser=new ClubAdvisor(newName,newEmail,newTele,newPassword);
                updatedUser.setClubAdvisorId(userId);
                CADataHandling obj=new CADataHandling();
                obj.updateClubAdvisorInDatabase(updatedUser);
                Main.currentUser=updatedUser;

                //Only for testing
                System.out.println(LoginAndRegistration.currentUserType);
                System.out.println(updatedUser);
                System.out.println(updatedUser.getClubAdvisorId());
                System.out.println(updatedUser.getName());
                System.out.println(updatedUser.getEmail());
                System.out.println(updatedUser.getMobileNum());
                System.out.println(updatedUser.getPassword());

            } else if (LoginAndRegistration.currentUserType.equals("STUDENT")) {
                Student updatedUser=new Student(newName,newEmail,newTele,newPassword);
                updatedUser.setStudentId(userId);
                StudentDataHandling obj= new StudentDataHandling();
                obj.updateStudentInDatabase(updatedUser);
                Main.currentStudentUser=updatedUser;

            }


        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            showErrorAlert(e.getMessage());
        }


    }

    //==============================================================
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
    //Overriding the above method. This method is to validate String inputs that doesn't have any character limit
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

    private boolean validatePasswordMatch() {
        if (userPasswordField1.getText().equals(userPasswordField2.getText())) {
            userPasswordField2Label.setText("Passwords match");
            userPasswordField2Label.setStyle("-fx-text-fill: green;");
            return true;
        } else {
            userPasswordField2Label.setText("Passwords do not match");
            userPasswordField2Label.setStyle("-fx-text-fill: red;");
            return false;
        }
    }

    private void setLabelStyle(String validationMessage, Label label) {
        if (validationMessage.equals("Valid")) {
            label.setStyle("-fx-text-fill: green;");
        } else {
            label.setStyle("-fx-text-fill: red;");
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
