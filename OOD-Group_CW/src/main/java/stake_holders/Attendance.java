package stake_holders;

public class Attendance {

    private Student student;
    private Events event;
    private boolean status;

    private String studentName;
    private String studentId;
    private String eventName;
    private String eventId;
    private Clubs parentClub;

    public Attendance(Student student, Events event, boolean status) {
        this.student = student;
        this.event = event;
        this.status = status;

        this.eventId=event.getEventId();
        this.eventName=event.getEventName();
        this.studentId=student.getStudentId();
        this.studentName=student.getName();

        this.parentClub=event.getParentClub();
    }
    //public Attendance (String studentId,String studentName,String eventId,)

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Clubs getParentClub() {
        return parentClub;
    }

    public void setParentClub(Clubs parentClub) {
        this.parentClub = parentClub;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
