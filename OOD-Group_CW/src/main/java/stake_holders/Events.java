package stake_holders;

import java.util.ArrayList;

public class Events {
    private String eventName;
    private String eventDate;
    private String eventLocation;
    private Clubs parentClub;
    private ArrayList<Student> attendance;

    public Events(String eventName, String eventDate, String eventLocation, Clubs parentClub) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.parentClub = parentClub;
    }

    public ArrayList<Student> getAttendance() {
        return attendance;
    }

    public void setAttendance(ArrayList<Student> attendance) {
        this.attendance = attendance;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}

