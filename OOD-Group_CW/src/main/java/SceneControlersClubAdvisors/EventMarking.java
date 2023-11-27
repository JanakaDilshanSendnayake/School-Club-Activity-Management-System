package SceneControlersClubAdvisors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import utils.MySqlConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class EventMarking {
    /*MySqlConnect ConnectNow = new MySqlConnect();
    Connection ConnectDB = ConnectNow.getDatabaseLink();*/
    ObservableList<EventMarking> register;
    private String StudentID;
    private String StudentName;
    private CheckBox checkBox;
    private Boolean Attendence;

    public EventMarking(String StudentID, String StudentName, CheckBox checkBox) {
        this.StudentID = StudentID;
        this.StudentName = StudentName;
        this.checkBox = checkBox;
    }
    public EventMarking(){}

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

    public ObservableList<String> getClubNames(Connection ConnectDB){
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/scams", "root", "#Wolf8me");
            ObservableList<String> clubChoices = FXCollections.observableArrayList();
            PreparedStatement ps = ConnectDB.prepareStatement("select club_name from Club;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                clubChoices.add(rs.getString("Club_name"));}
            //ClubSelectionChoiceBox.getItems().addAll(rs.getString("club_name"));}
            return clubChoices;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<String> getEventNames(Connection ConnectDB,String clubName){
        ObservableList<String> eventChoices = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = ConnectDB.prepareStatement("select event_name\n" +
                    "from Event\n" +
                    "inner join Club\n" +
                    "on Event.club_id = Club.club_id\n" +
                    "where club_name = '"+(clubName)+"';");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventChoices.add(rs.getString("event_name"));
                //EventSelectionChoiceBox.getItems().addAll(rs.getString("event_name"));
            }return eventChoices;
        }catch (Exception e){e.printStackTrace(); return null;}
    }


    public ObservableList<EventMarking> createRegister(Connection ConnectDB,String clubName){
        register = FXCollections.observableArrayList();
        try{
            PreparedStatement ps1 = ConnectDB.prepareStatement("select Student.Student_id, Student_name\n" +
                "from Student_club\n" +
                "inner join student\n" +
                "on Student_club.student_id = student.student_id\n" +
                "inner join club\n" +
                "on student_club.club_id = club.club_id\n" +
                "where club_name = '"+(clubName)+"';");
            ResultSet rs1 = ps1.executeQuery();
            while(rs1.next()){
                CheckBox test = new CheckBox();
                register.add(new EventMarking(rs1.getString("Student_id"),rs1.getString("Student_name"),test));
            //checkBoxes.add(test);
         }return register;
        }catch (Exception e){
            e.printStackTrace();return null;}
    }
    String event_id;
    public void saveAttendence(Connection ConnectDB, String eventName){
        try{
        PreparedStatement statement = ConnectDB.prepareStatement("select Event_id \n" +
                "from event\n" +
                "where event_name = '"+(eventName)+"';");
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            event_id = rs.getString("Event_id");}
        System.out.println(event_id);
// clear previos values
        PreparedStatement ps = ConnectDB.prepareStatement("delete\n" +
                "from attendance\n" +
                "where event_id = '"+(event_id)+"';");
        ps.executeUpdate();
// update attendence table
        //List<String> presentStudents = new ArrayList<>();
        for (EventMarking student:register) {
            if(student.getCheckBox().isSelected()){
                PreparedStatement ps1 = ConnectDB.prepareStatement("insert into attendance(student_id, event_id, attendance_status) values ('"+(student.getStudentID())+"','"+(event_id)+"',"+(true)+");");
                ps1.executeUpdate();// insert bool = True

                //presentStudents.add(student.getStudentName()+" "+student.getStudentID());
            }else{PreparedStatement ps1 = ConnectDB.prepareStatement("insert into attendance(student_id, event_id, attendance_status) values ('"+(student.getStudentID())+"','"+(event_id)+"',"+(false)+");");
                ps1.executeUpdate();}// insert bool = False
        }
        }catch (Exception e){e.printStackTrace();}
    }


    public EventMarking(String studentID, String studentName, Boolean Attendence) {
        this.StudentID = studentID;
        this.StudentName = studentName;
        this.Attendence = Attendence;
    }

    public ObservableList<EventMarking> getRegister(Connection ConnectDB, String club_name, String event_name){
        ObservableList<EventMarking> attendenceRegister = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = ConnectDB.prepareStatement("select student.student_id, student_name, Attendance_status\n" +
                    "from attendance\n" +
                    "inner join student\n" +
                    "on attendance.student_id = student.student_id\n" +
                    "inner join event\n" +
                    "on event.event_id = attendance.event_id\n" +
                    "inner join club\n" +
                    "on club.club_id = event.club_id\n" +
                    "where club_name = '"+(club_name)+"' and event_name = '"+(event_name)+"';");// + (club) + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                attendenceRegister.add(new EventMarking(rs.getString("student_id"), rs.getString("student_name"), rs.getBoolean("attendance_status") ));
                //NameAttendence.add(new EventMarking(rs.getString(),rs.getString(),rs.getBoolean()));
            }return attendenceRegister;
        }catch (Exception e){e.printStackTrace();return null;}
    }

    public Boolean getAttendence() {
        return Attendence;
    }

    public void setAttendence(Boolean attendence) {
        Attendence = attendence;
    }

    /*public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        this.StudentName = studentName;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        this.StudentID = studentID;
    }*/
}

