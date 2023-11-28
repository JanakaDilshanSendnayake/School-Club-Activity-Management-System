package stake_holders;

import utils.StudentDataHandling;

import java.util.ArrayList;

public class Student extends Users {
    private String studentId;
    private ArrayList<Clubs> joinedClubs;

    //REGEX
    private static final String STUDENT_ID_REGEX = "^[a-zA-Z0-9]{1,10}$";

    public Student(String studentId, String name, String email, String mobileNumber, String password) {
        super(name, email, mobileNumber, password); // Call the constructor of the parent class
        validateUserId(studentId);

        this.studentId = studentId;
        joinedClubs = new ArrayList<>();
    }

    public Student(String name, String email, String mobileNumber, String password) {
        super(name, email, mobileNumber, password);
        // Constructor for loading data from the database
        joinedClubs = new ArrayList<>();
    }
    @Override
    protected void validateUserId(String studentId) {
        StudentDataHandling student=new StudentDataHandling();
        if (!studentId.matches(STUDENT_ID_REGEX)&&student.studentUserNameValidation(studentId)) {
            throw new IllegalArgumentException("Invalid Club Advisor ID format: " + studentId);
        }
    }


    public String getStudentId() {
        return studentId;
    }

    public ArrayList<Clubs> getJoinedClubs() {
        return joinedClubs;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public void setJoinedClubs(ArrayList<Clubs> joinedClubs) {
        this.joinedClubs = joinedClubs;
    }
}
