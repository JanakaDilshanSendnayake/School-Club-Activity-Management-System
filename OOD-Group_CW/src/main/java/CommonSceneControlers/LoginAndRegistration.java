package CommonSceneControlers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;
import stake_holders.ClubAdvisor;
import stake_holders.Student;
import utils.CADataHandling;
import utils.StudentDataHandling;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
//BY JANAKA DILSHAN SENDANAYAKE-RGU 2237952

public class LoginAndRegistration extends BaseSceneController implements Initializable {

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //start page will be automatically loaded to front
        startPage.toFront();
        loginPageTextlabel.setVisible(false);
        loginPageTextlabel1.setVisible(false);

        // Initializing Event listener for Club Advisor ID
        newUserIdFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateUserID(newValue);
            newUserIdLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newUserIdLabel);
        });

        // Initializing Event listener for Club Advisor First Name
        newUserFirstNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, FIRST_NAME_REGEX,
                    "Names can only contain letters.",10);
            newUserFirstNameLabel.setText(validationMessage);
            setLabelStyle(validationMessage, newUserFirstNameLabel);
        });

        // Initializing Event listener for Club Advisor Last Name
        newUserLastNameFieldListener=((observable, oldValue, newValue) -> {
            String validationMessage = validateTextField(newValue, LAST_NAME_REGEX,
                    "Names can only contain letters.",20);
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
    //This method is to validate the user Id entered by the user. It checks whether user input follows regex, is it less 10-
    // and most importantly it checks the database constantly to see if there's someone using the same username.
    private String validateUserID(String value) {
        CADataHandling clubAdvisor=new CADataHandling();
        StudentDataHandling student=new StudentDataHandling();
        if((Objects.equals(currentUserType, "CLUB-ADVISOR") && clubAdvisor.clubAdvisorUserNameValidation(value))||(Objects.equals(currentUserType, "STUDENT") && student.studentUserNameValidation(value))){
            return "Sorry this user name is already taken";
        }if (value.matches(USER_ID_REGEX)) {
            return "Valid";
        }
        if (value.length() > 10) {
            return "Maximum character limit exceeded";
        }
        return "User ID should be a combination of letters and numbers.";
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
    //These functions assigned to every textfeild. When a user click on textfiled and starts to enter data these methods get
    // -triggered. Then it adds the relevant event listener to the text filed. The reason we adding the the event litners seperatly
    //-is we can remove them when ever we want.
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
        //getting the user inputs from the text fields
        String newId=newUserIdField.getText();
        String newName= newUserFirstNameField.getText().toLowerCase()+"_"+newUserLastNameField.getText().toLowerCase();
        String newEmail=newUserEmailField.getText();
        String newTele=newUserTeleField.getText();
        String newPassword;
        if(validatePasswordMatch()){newPassword=newUserPasswordField1.getText();}else{newPassword="invalid";}
        try{
            if(currentUserType.equals("CLUB-ADVISOR")){
                ClubAdvisor newUser=new ClubAdvisor(newId,newName,newEmail,newTele,newPassword);
                CADataHandling obj=new CADataHandling();
                obj.saveNewCAToDatabase(newUser);
                showInfoAlert("You successfully registered to the system.");
            } else if (currentUserType.equals("STUDENT")) {
                Student newUser=new Student(newId,newName,newEmail,newTele,newPassword);
                StudentDataHandling obj=new StudentDataHandling();
                obj.saveStudentToDatabase(newUser);
                showInfoAlert("You successfully registered to the system.");
            }
            loginPage.toFront();
            clearUserInputsFieldsInRegistrationPage();
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            showErrorAlert(e.getMessage());
        }

    }
    //This method is assigned to clear button. This will set all the textfields and text labels to "" and they will remove-
    //-event listeners also
    @FXML
    private void clearUserInputsFieldsInRegistrationPage(){
        newUserIdField.textProperty().removeListener(newUserIdFieldListener);
        newUserFirstNameField.textProperty().removeListener(newUserFirstNameFieldListener);
        newUserLastNameField.textProperty().removeListener(newUserLastNameFieldListener);
        newUserEmailField.textProperty().removeListener(newUserEmailFieldListener);
        newUserTeleField.textProperty().removeListener(newUserTeleFieldListener);
        newUserPasswordField1.textProperty().removeListener(newUserPassword1Listener);
        newUserPasswordField2.textProperty().removeListener(newUserPassword2Listener);
        newUserIdField.setText("");
        newUserFirstNameField.setText("");
        newUserFirstNameLabel.setText("");
        newUserLastNameField.setText("");
        newUserLastNameLabel.setText("");
        newUserEmailField.setText("");
        newUserTeleField.setText("");
        newUserPasswordField1.setText("");
        newUserPasswordField2.setText("");
        newUserIdLabel.setText("");
        newUserFirstNameLabel.setText("");
        newUserEmailLabel.setText("");
        newUserTeleLabel.setText("");
        newUserPasswordField1Label.setText("");
        newUserPasswordField2Label.setText("");
    }
    @FXML
    private TextField loginUserNameField;
    @FXML
    private PasswordField loginPasswordField;
    private Stage stage;
    private Scene scene;
    private Parent root;

    //when user enters password and username, this method validates them and identifies whether user is a club advisor-
    //-or a student and lead the user to their relevant menu.
    @FXML
    private void login(ActionEvent actionEvent){
        CADataHandling object=new CADataHandling();
        StudentDataHandling object2=new StudentDataHandling();
        if(currentUserType.equals("CLUB-ADVISOR") && object.clubAdvisorLogin(loginUserNameField.getText(),loginPasswordField.getText())){
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml_files/ClubAdvisor/Menu-ClubAdvisor.fxml"));
                scene = new Scene(root);
                stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }catch (Exception e){
                showErrorAlert(e.getMessage());
            }
        } else if (currentUserType.equals("STUDENT")&&object2.studentLogin(loginUserNameField.getText(),loginPasswordField.getText())) {
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml_files/Student/Menu-Students.fxml"));
                scene = new Scene(root);
                stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }catch (Exception e){
                e.printStackTrace();;
                showErrorAlert(e.getMessage());
            }
        }else {
            showErrorAlert("The login credentials you entered are wrong");
        }
    }

    //***Special-this static variable tracks weather if the current user is a student or a club advisor
    public static String currentUserType;

    @FXML
    private ImageView loginPageIcon;
    @FXML
    private ImageView registerPageIcon;
    @FXML
    private Label loginPageTextlabel;
    @FXML
    private Label loginPageTextlabel1;
    @FXML
    private Label registerPageTextLabel;
    @FXML
    private AnchorPane loginPage;
    @FXML
    private AnchorPane startPage;
//These methods are to change some ui elements according to the current user type(STUDENT/CLUB-ADVISOR) using the system
    @FXML
    private void selectClubAdvisor(){
        currentUserType="CLUB-ADVISOR";
        loginPageTextlabel.setVisible(true);
        loginPageTextlabel1.setVisible(false);
        registerPageTextLabel.setText("Club Advisor Registration");
        Image clubAdvisorIcon = new Image(getClass().getResourceAsStream("/Icons/club advisor.png"));
        loginPageIcon.setImage(clubAdvisorIcon);
        registerPageIcon.setImage(clubAdvisorIcon);
        loginPage.toFront();

    }
    @FXML
    private void selectStudent(){
        currentUserType="STUDENT";
        loginPageTextlabel.setVisible(false);
        loginPageTextlabel1.setVisible(true);
        registerPageTextLabel.setText("Student Registration");
        Image studentIcon = new Image(getClass().getResourceAsStream("/Icons/child-head.png"));
        loginPageIcon.setImage(studentIcon);
        registerPageIcon.setImage(studentIcon);
        loginPage.toFront();

    }
    //To switch between different anchor panes of the same scene
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
        if(showConfirmationAlert("Are you sure that you want cancel the registration process? ")){
            loginPage.toFront();
            clearUserInputsFieldsInRegistrationPage();
        }

    }


}


