package stake_holders;

import java.time.LocalDate;
import java.util.ArrayList;

public class Events {
    private String eventId;
    private String eventName;
    private LocalDate eventDate;
    private String eventLocation;
    private String eventDescription;
    private Clubs parentClub;
    private ArrayList<Student> attendedStudents;
    private ArrayList<Student> unAttendedStudents;


    private static final String EVENT_NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String EVENT_DESCRIPTION_REGEX = "^.{1,500}$";
    private static final String EVENT_VENUE_REGEX = "^.{1,50}$";


    public Events(String eventId,String eventName, LocalDate eventDate, String eventLocation,String eventDescription, Clubs parentClub) {
        validateEventName(eventName);
        validateEventDate(eventDate);
        validateEventVenue(eventLocation);
        validateEventDescription(eventDescription);

        this.eventId=eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventDescription=eventDescription;
        this.parentClub = parentClub;

        this.attendedStudents=new ArrayList<>();
        this.unAttendedStudents=new ArrayList<>();

    }

    private void validateEventName(String name){
        if(name.isEmpty()){
            throw new IllegalArgumentException("You haven't filled Event name. It's mandatory");
        }
        if(!name.matches(EVENT_NAME_REGEX)){
            throw new IllegalArgumentException("Invalid event format: " + name);
        }
    }
    private void validateEventDescription(String description){
        if(description.equals("")){
            throw new IllegalArgumentException("You haven't filled event description. It's mandatory");
        }else{
            if(!description.matches(EVENT_DESCRIPTION_REGEX)){
                throw new IllegalArgumentException("You have exceeded the maximum character limit.");
            }
        }
    }
    private void validateEventVenue(String description){
        if(description.isEmpty()){
            throw new IllegalArgumentException("You haven't filled event venue. It's mandatory");
        }
        if(!description.matches(EVENT_VENUE_REGEX)){
            throw new IllegalArgumentException("You have exceeded the maximum character limit.");
        }

    }
    private void validateEventDate(LocalDate date){
        if(date==null){
            throw new IllegalArgumentException("You haven't selected event date. It's mandatory");
        }
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Clubs getParentClub() {
        return parentClub;
    }

    public void setParentClub(Clubs parentClub) {
        this.parentClub = parentClub;
    }

    public ArrayList<Student> getAttendedStudents() {
        return attendedStudents;
    }

    public void setAttendedStudents(ArrayList<Student> attendedStudents) {
        this.attendedStudents = attendedStudents;
    }

    public ArrayList<Student> getUnAttendedStudents() {
        return unAttendedStudents;
    }

    public void setUnAttendedStudents(ArrayList<Student> unAttendedStudents) {
        this.unAttendedStudents = unAttendedStudents;
    }
    //=======================================================

//    private ArrayList<Attendance> eventAttendance;
//    public void attendanceMarking(){
//
//        for(Student student:this.parentClub.getStudentMembers()){
//            this.eventAttendance.add(new Attendance(student,this,false));
//        }
//    }
//
//    public ArrayList<Attendance> getEventAttendance() {
//        return eventAttendance;
//    }
//
//    public void setEventAttendance(ArrayList<Attendance> eventAttendance) {
//        this.eventAttendance = eventAttendance;
//    }
}

