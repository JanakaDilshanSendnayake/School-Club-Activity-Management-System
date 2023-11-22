package stake_holders;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String mobileNumber;
    private String password;

    public Student(String studentId, String name, String email, String mobileNumber, String password) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
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
