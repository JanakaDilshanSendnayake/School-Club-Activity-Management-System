package SceneControlersClubAdvisors;

import javafx.scene.control.CheckBox;

public class NameAttendence {
    private String studentID;
    private String studentName;
    private Boolean Attendence;

    public NameAttendence(String studentID, String studentName, Boolean Attendence) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.Attendence = Attendence;
    }

    public Boolean getAttendence() {
        return Attendence;
    }

    public void setAttendence(Boolean attendence) {
        Attendence = attendence;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}
