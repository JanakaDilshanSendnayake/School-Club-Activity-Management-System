package SceneControlersClubAdvisors;

import javafx.scene.control.CheckBox;

public class NameAttendence {
    private String StudentID;
    private String StudentName;
    private CheckBox checkBox;

    public NameAttendence(String StudentID,String StudentName,CheckBox checkBox) {
        this.StudentID = StudentID;
        this.StudentName = StudentName;
        this.checkBox = checkBox;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }
}
