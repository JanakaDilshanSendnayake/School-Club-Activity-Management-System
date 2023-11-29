package CommonSceneControlers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.Main;
import stake_holders.ClubAdvisor;
import stake_holders.Student;
import utils.CADataHandling;
import utils.StudentDataHandling;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * BY JANAKA SENDANAYAKE RGU ID:2237952
 */
public class AccountManager extends BaseSceneController implements Initializable {

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
    @FXML
    private Label userPasswordField2Label2;

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

        //Locking the text fields when page is being load. Users can just view data but they needs to press unlock button-
        // -to unlock these fields and start editing
        userIdField.setEditable(false);
        userFirstNameField.setEditable(false);
        userLastNameField.setEditable(false);
        userEmailField.setEditable(false);
        userTeleField.setEditable(false);
        userPasswordField1.setEditable(false);
        userPasswordField2.setEditable(false);
        userPasswordField2.setVisible(false);
        userPasswordField2Label2.setVisible(false);

        saveButton.setVisible(false);
        cancelTheUpdateButton.setVisible(false);
        updateButton.setVisible(true);

        onGoingAccountDetailsUpdate=false;

        //Setting up the details of current users to textfields. The passwords are not shown until user unlocks the texfields
        if(LoginAndRegistration.currentUserType.equals("CLUB-ADVISOR")){
            userIdField.setText(Main.currentUser.getClubAdvisorId());
            String[] fullName=Main.currentUser.getName().split("_");
            userFirstNameField.setText(fullName[0]);
            userLastNameField.setText(fullName[1]);
            userEmailField.setText(Main.currentUser.getEmail());
            userTeleField.setText(Main.currentUser.getMobileNumber());
            userPasswordField1.setText("************");
        } else if (LoginAndRegistration.currentUserType.equals("STUDENT")) {
            userIdField.setText(Main.currentStudentUser.getStudentId());
            String[] fullName=Main.currentStudentUser.getName().split("_");
            userFirstNameField.setText(fullName[0]);
            userLastNameField.setText(fullName[1]);
            userEmailField.setText(Main.currentStudentUser.getEmail());
            userTeleField.setText(Main.currentStudentUser.getMobileNumber());
            userPasswordField1.setText("************");
        }


        // Initializing Event listener for User First Name
        userFirstNameFieldListener =((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, FIRST_NAME_REGEX,
                    "Name can only contain letters.",10);
            userFirstNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, userFirstNameLabel);
        });

        // Initializing Event listener for User Last Name
        userLastNameFieldListener =((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, LAST_NAME_REGEX,
                    "Name can only contain letters.",20);
            userLastNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, userLastNameLabel);
        });

        // Initializing Event listener for User Email
        userEmailFieldListener =((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue,
                    EMAIL_REGEX, "Invalid email format.");
            userEmailLabel.setText(validationMessage);
            setLabelStyle(validationMessage, userEmailLabel);
        });

        // Initializing Event listener for User Telephone
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
    //These functions assigned to every text feild. When a user click on text filed and starts to enter data these methods get
    // -triggered. Then it adds the relevant event listener to the text filed. The reason we are adding the event litners seperatly
    //-is we can remove them whenever we want
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
    private boolean onGoingAccountDetailsUpdate;

    //Unlocking the text fields to update
    @FXML
    private void update(){
        if(showConfirmationAlert("Are you sure that you want to start updating account details?")){
            if (LoginAndRegistration.currentUserType.equals("CLUB-ADVISOR")) {
                userPasswordField1.setText(Main.currentUser.getPassword());
                userPasswordField2.setText(Main.currentUser.getPassword());
            } else if (LoginAndRegistration.currentUserType.equals("STUDENT")) {
                userPasswordField1.setText(Main.currentStudentUser.getPassword());
                userPasswordField2.setText(Main.currentStudentUser.getPassword());
            }
            //Adding the event listeners to the text fields.
            handleNewUserFirstNameChange();
            handleNewUserLastNameChange();
            handleNewUserEmailChange();
            handleNewUserTeleChange();
            onGoingAccountDetailsUpdate=true;
            //Unlocking the text fields and hiding some buttons and setvisible some buttons
            userFirstNameField.setEditable(true);
            userLastNameField.setEditable(true);
            userEmailField.setEditable(true);
            userPasswordField1.setEditable(true);
            userPasswordField1.setEditable(true);
            userPasswordField2.setVisible(true);
            userPasswordField2Label2.setVisible(true);
            saveButton.setVisible(true);
            cancelTheUpdateButton.setVisible(true);
            updateButton.setVisible(false);
        }

    }

    @FXML
    private void saveTheUpdate(ActionEvent actionEvent){
        if (showConfirmationAlert("Are you sure that you want to save these updated details?")) {
            String userId = userIdField.getText();
            String newName = userFirstNameField.getText().toLowerCase() + "_" + userLastNameField.getText().toLowerCase();
            String newEmail = userEmailField.getText();
            String newTele = userTeleField.getText();
            String newPassword;
            if (validatePasswordMatch()) {
                newPassword = userPasswordField1.getText();
            } else {
                newPassword = "invalid";
            }
            //To validate users we try to make a student or a ca object inside a try block-
            // -The validations are done inside the constructors of student or ca class using the validation m
            //methods implemented in those classes.If user inputs are invalid then those validation methods will throw
            //IllegalArgumentExceptions.
            try {
                if (LoginAndRegistration.currentUserType.equals("CLUB-ADVISOR")) {
                    ClubAdvisor updatedUser = new ClubAdvisor(newName, newEmail, newTele, newPassword);
                    updatedUser.setClubAdvisorId(userId);
                    CADataHandling obj = new CADataHandling();
                    obj.updateClubAdvisorInDatabase(updatedUser);
                    Main.currentUser = updatedUser;
                    showInfoAlert("Updated details saved successfully");
                    loadAccounts(actionEvent);
                } else if (LoginAndRegistration.currentUserType.equals("STUDENT")) {
                    Student updatedUser = new Student(newName, newEmail, newTele, newPassword);
                    updatedUser.setStudentId(userId);
                    StudentDataHandling obj = new StudentDataHandling();
                    obj.updateStudentInDatabase(updatedUser);
                    Main.currentStudentUser = updatedUser;
                    loadStudentAccounts(actionEvent);
                    showInfoAlert("Updated details saved successfully");
                }
            } catch (IllegalArgumentException e) {
                showErrorAlert(e.getMessage());
            }
        }
    }
    //Update cancelation method. Gets user's confirmation before doing so.
    @FXML
    private void cancel(ActionEvent actionEvent){
        if(showConfirmationAlert("Are you sure that you want cancel the ongoing update?")) {
            if(LoginAndRegistration.currentUserType.equals("CLUB-ADVISOR")) {
                loadAccounts(actionEvent);
            }else if(LoginAndRegistration.currentUserType.equals("STUDENT")) {
                loadStudentAccounts(actionEvent);
            }
        }
    }
    //validating whether passwords in password field and password re enter fields
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
    //Implemented some new sceneloader methods using scene loader methods in base scene controller to prevent-
    //-user from switching scenes if there's an on going detail update
    @FXML
    private void loadHomeFromAccountManger(ActionEvent actionEvent){
        if (!onGoingAccountDetailsUpdate || showConfirmationAlert("Are you sure you want to cancel the ongoing update?")) {
            if(LoginAndRegistration.currentUserType.equals("CLUB-ADVISOR")) {
                loadHome(actionEvent);
            }else if(LoginAndRegistration.currentUserType.equals("STUDENT")) {
                loadStudentHome(actionEvent);
            }
        }
    }

    @FXML
    private void loadClubsFromAccountManger(ActionEvent actionEvent){
        if (!onGoingAccountDetailsUpdate || showConfirmationAlert("Are you sure you want to cancel the ongoing update?")) {
            if(LoginAndRegistration.currentUserType.equals("CLUB-ADVISOR")) {
                loadClubs(actionEvent);
            }else if(LoginAndRegistration.currentUserType.equals("STUDENT")) {
                loadStudentClubs(actionEvent);
            }
        }
    }

    @FXML
    private void loadEventsFromAccountManger(ActionEvent actionEvent){
        if (!onGoingAccountDetailsUpdate || showConfirmationAlert("Are you sure you want to cancel the ongoing update?")) {
            if(LoginAndRegistration.currentUserType.equals("CLUB-ADVISOR")) {
                loadEvents(actionEvent);
            }else if(LoginAndRegistration.currentUserType.equals("STUDENT")) {
                loadStudentEvents(actionEvent);
            }
        }
    }

    @FXML
    private void loadReportsFromAccountManger(ActionEvent actionEvent){
        if (!onGoingAccountDetailsUpdate || showConfirmationAlert("Are you sure you want to cancel the ongoing update?")) {
            loadReports(actionEvent);
        }
    }

    @FXML
    private void loadAccountsFromAccountManger(ActionEvent actionEvent){
        if (!onGoingAccountDetailsUpdate || showConfirmationAlert("Are you sure you want to cancel the ongoing update?")) {
            if(LoginAndRegistration.currentUserType.equals("CLUB-ADVISOR")) {
                loadAccounts(actionEvent);
            }else if(LoginAndRegistration.currentUserType.equals("STUDENT")) {
                loadStudentAccounts(actionEvent);
            }
        }
    }

}
