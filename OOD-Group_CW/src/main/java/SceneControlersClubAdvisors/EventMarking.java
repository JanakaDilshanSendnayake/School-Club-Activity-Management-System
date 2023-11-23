package SceneControlersClubAdvisors;

import javafx.scene.control.CheckBox;

/**
 * Controller class for
 */
public class EventMarking {
    private String StudentID;
    private String StudentName;
    private CheckBox checkBox;

    public EventMarking(String StudentID, String StudentName, CheckBox checkBox) {
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
