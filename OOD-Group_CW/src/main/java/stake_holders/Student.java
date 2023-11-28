package stake_holders;

import utils.StudentDataHandling;

import java.util.ArrayList;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String mobileNumber;
    private String password;
    private ArrayList<Clubs> joinedClubs;

    //REGEX
    private static final String STUDENT_ID_REGEX = "^[a-zA-Z0-9]{1,10}$";
    private static final String NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@iit\\.ac\\.lk$";
    private static final String MOBILE_NUMBER_REGEX = "^\\d{10}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";

    public Student(String studentId, String name, String email, String mobileNumber, String password) {
        validateSTUDENTId(studentId);
        validateName(name);
        validateEmail(email);
        validateMobileNumber(mobileNumber);
        validatePassword(password);

        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;

        joinedClubs=new ArrayList<>();
    }
    //To load data from database
    public Student(String name, String email, String mobileNumber, String password) {

        validateName(name);
        validateEmail(email);
        validateMobileNumber(mobileNumber);
        validatePassword(password);

        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;

        joinedClubs=new ArrayList<>();
    }

    private void validateSTUDENTId(String studentId) {
        StudentDataHandling student=new StudentDataHandling();
        if (studentId.isEmpty()) {
            throw new IllegalArgumentException("Club advisor ID is mandatory and cannot be empty");
        }
        if (!studentId.matches(STUDENT_ID_REGEX)) {
            throw new IllegalArgumentException("Invalid Club Advisor ID format: " + studentId);
        }
        if (student.studentUserNameValidation(studentId)) {
            throw new IllegalArgumentException("This username is already being used.");
        }
    }

    private void validateName(String name) {
        if(name.endsWith("_")){
            throw new IllegalArgumentException("You haven't filled full name. It's mandatory");
        }
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid name format: " + name);
        }
    }

    private void validateEmail(String email) {
        if(email.isEmpty()){
            throw new IllegalArgumentException("You haven't filled email. It's mandatory");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    private void validateMobileNumber(String mobileNum) {
        if(mobileNum.isEmpty()){
            throw new IllegalArgumentException("You haven't filled mobile number. It's mandatory");
        }
        if (!mobileNum.matches(MOBILE_NUMBER_REGEX)) {
            throw new IllegalArgumentException("Invalid mobile number format: " + mobileNum);
        }
    }

    private void validatePassword(String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException("Invalid password format: " + password);
        }
    }
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Clubs> getJoinedClubs() {
        return joinedClubs;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJoinedClubs(ArrayList<Clubs> joinedClubs) {
        this.joinedClubs = joinedClubs;
    }
}
