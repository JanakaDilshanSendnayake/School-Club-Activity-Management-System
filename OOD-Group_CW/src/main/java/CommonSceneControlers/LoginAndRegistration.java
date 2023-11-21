package CommonSceneControlers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import stake_holders.ClubAdvisor;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginAndRegistration implements Initializable {

    //REGEX
    //Username should be a combination letters and numbers
    private static final String USER_ID_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{1,10}$";
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

    //The main anchor pain holding all other elements of user registration page
    @FXML
    private AnchorPane userRegistrationPane;

    //Text fields to get the details from new users who are joining the system
    @FXML
    private TextField newUserIdField;
    @FXML
    private TextField newUserFirstNameField;
    @FXML
    private TextField newUserLastNameField;
    @FXML
    private TextField newUserEmailField;
    @FXML
    private TextField newUserTeleField;
    @FXML
    private TextField newUserPasswordField1;
    @FXML
    private TextField newUserPasswordField2;

    //Text labels to show the relevant information about the inputs they have given to above text fields in real time
    @FXML
    private Label newUserIdLabel;
    @FXML
    private Label newUserFirstNameLabel;
    @FXML
    private Label newUserLastNameLabel;
    @FXML
    private Label newUserEmailLabel;
    @FXML
    private Label newUserTeleLabel;
    @FXML
    private Label newUserPasswordField1Label;
    @FXML
    private Label newUserPasswordField2Label;

    //event listeners to validate and show the users messages about their inputs in realtime.
    //For example if user enters a invalid input to the username field, it will show a message saying that the input is-
    //-wrong in the text label which is under the username field. these will be initialized in the initialize method and-
    //-will be later assigned to the fields
    private ChangeListener<String> newUserIdFieldListener;
    private ChangeListener<String> newUserFirstNameFieldListener;
    private ChangeListener<String> newUserLastNameFieldListener;
    private ChangeListener<String> newUserEmailFieldListener;
    private ChangeListener<String> newUserTeleFieldListener;
    private ChangeListener<String> newUserPassword1Listener;
    private ChangeListener<String> newUserPassword2Listener;

    //Button


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startPage.toFront();
//        Platform.runLater(() -> userRegistrationPane.requestFocus());

        // Initializing Event listener for Club Advisor ID
        newUserIdFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, USER_ID_REGEX,
                    "User ID should be a combination letters and numbers.",10);
            newUserIdLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newUserIdLabel);
        });

        // Initializing Event listener for Club Advisor First Name
        newUserFirstNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, FIRST_NAME_REGEX,
                    "Name can only contain letters.",10);
            newUserFirstNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newUserFirstNameLabel);
        });

        // Initializing Event listener for Club Advisor Last Name
        newUserLastNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, LAST_NAME_REGEX,
                    "Name can only contain letters.",20);
            newUserLastNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newUserLastNameLabel);
        });

        // Initializing Event listener for Club Advisor Email
        newUserEmailFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue,
                    EMAIL_REGEX, "Invalid email format.");
            newUserEmailLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newUserEmailLabel);
        });

        // Initializing Event listener for Club Advisor Telephone
        newUserTeleFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, MOBILE_NUMBER_REGEX,
                    "Invalid mobile number format.");
            newUserTeleLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newUserTeleLabel);
        });

        // Initializing Event listener for Password Field 1
        newUserPassword1Listener=((observable, oldValue, newValue) -> {
            String validationMessage = validatePasswordField(newValue, PASSWORD_REGEX);
            newUserPasswordField1Label.setText(validationMessage);
            setLabelStyle(validationMessage, newUserPasswordField1Label);
            validatePasswordMatch();
        });

        // Initializing Event listener for Password Field 2
        newUserPassword2Listener=((observable, oldValue, newValue) -> {
            validatePasswordMatch();
        });
    }

    //Method to validate string inputs with a character limit
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
    //Method to check if the passwords in both password and Re enter password fields matches
    private boolean validatePasswordMatch() {
        if (newUserPasswordField1.getText().equals(newUserPasswordField2.getText())) {
            newUserPasswordField2Label.setText("Passwords match");
            newUserPasswordField2Label.setStyle("-fx-text-fill: green;");
            return true;
        } else {
            newUserPasswordField2Label.setText("Passwords do not match");
            newUserPasswordField2Label.setStyle("-fx-text-fill: red;");
            return false;
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
    private void handleNewUserIdChange() {
        newUserIdField.textProperty().addListener(newUserIdFieldListener);
    }
    @FXML
    private void handleNewUserFirstNameChange() {
        newUserFirstNameField.textProperty().addListener(newUserFirstNameFieldListener);
    }
    @FXML
    private void handleNewUserLastNameChange(){
        newUserLastNameField.textProperty().addListener(newUserLastNameFieldListener);
    }
    @FXML
    private void handleNewUserEmailChange() {
        newUserEmailField.textProperty().addListener(newUserEmailFieldListener);
    }
    @FXML
    private void handleNewUserTeleChange() {
        newUserTeleField.textProperty().addListener(newUserTeleFieldListener);
    }
    @FXML
    private void handleNewUserPassword1Change() {
        newUserPasswordField1.textProperty().addListener(newUserPassword1Listener);
    }
    @FXML
    private void handleNewUserPassword2Change() {
        newUserPasswordField2.textProperty().addListener(newUserPassword2Listener);
    }
    @FXML
    private void register(){
        String newId=newUserIdField.getText();
        String newName= newUserFirstNameField.getText().toLowerCase()+"_"+newUserLastNameField.getText().toLowerCase();
        String newEmail=newUserEmailField.getText();
        String newTele=newUserTeleField.getText();
        String newPassword;
        if(validatePasswordMatch()){newPassword=newUserPasswordField1.getText();}else{newPassword="invalid";}
        try{
            ClubAdvisor newUser=new ClubAdvisor(newId,newName,newEmail,newTele,newPassword);

            //Should implement a method that takes above object as an argument and save the data in relevent table fields

            //Only for testing
            System.out.println(currentUserType);
            System.out.println(newUser);
            System.out.println(newUser.getClubAdvisorId());
            System.out.println(newUser.getName());
            System.out.println(newUser.getEmail());
            System.out.println(newUser.getMobileNum());
            System.out.println(newUser.getPassword());
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

    public String currentUserType;

    @FXML
    private ImageView loginPageIcon;
    @FXML
    private ImageView registerPageIcon;
    @FXML
    private Label loginPageTextlabel;
    @FXML
    private Label registerPageTextLabel;
    @FXML
    private AnchorPane loginPage;
    @FXML
    private AnchorPane startPage;

    @FXML
    private void selectClubAdvisor(){
        currentUserType="CLUBADVISOR";
        loginPageTextlabel.setText("CLUB ADVISOR LOGIN");
        registerPageTextLabel.setText("Club Advisor Registration");
        Image clubAdvisorIcon = new Image(getClass().getResourceAsStream("/Icons/club advisor.png"));
        loginPageIcon.setImage(clubAdvisorIcon);
        registerPageIcon.setImage(clubAdvisorIcon);
        loginPage.toFront();

    }
    @FXML
    private void selectStudent(){
        currentUserType="STUDENT";
        loginPageTextlabel.setText("STUDENT LOGIN");
        registerPageTextLabel.setText("Student Registration");
        Image studentIcon = new Image(getClass().getResourceAsStream("/Icons/child-head.png"));
        loginPageIcon.setImage(studentIcon);
        registerPageIcon.setImage(studentIcon);
        loginPage.toFront();

    }
    @FXML
    private void selectRegister(){
        userRegistrationPane.toFront();
    }
    @FXML
    private void backToStartFromLogin(){
        startPage.toFront();
    }
    @FXML
    private void backToLoginFromRegistration(){
        loginPage.toFront();
    }

}


