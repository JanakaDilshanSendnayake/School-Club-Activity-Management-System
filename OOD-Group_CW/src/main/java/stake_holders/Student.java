package stake_holders;

import utils.StudentDataHandling;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String mobileNumber;
    private String password;

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
    }

    private void validateSTUDENTId(String studentId) {
        StudentDataHandling student=new StudentDataHandling();
        if (!studentId.matches(STUDENT_ID_REGEX)&&student.studentUserNameValidation(studentId)) {
            throw new IllegalArgumentException("Invalid Club Advisor ID format: " + studentId);
        }
    }

    private void validateName(String name) {
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid name format: " + name);
        }
    }

    private void validateEmail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    private void validateMobileNumber(String mobileNum) {
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
}
