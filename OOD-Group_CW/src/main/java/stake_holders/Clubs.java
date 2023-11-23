package stake_holders;

import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Class which holds the variables and methods of club
 */
public class Clubs {

    private String clubName;
    private String clubId;
    private String clubType;
    private String clubDescription;
    private ClubAdvisor clubAdmin;
    private ArrayList<ClubAdvisor> clubAdvisorMembers;
    private ArrayList<Student> studentMembers;
    private ArrayList<Events> clubEvents;


    public Clubs(String clubName, String clubId, ClubAdvisor creatorCA) {
        clubAdvisorMembers=new ArrayList<>();
        this.clubName = clubName;
        this.clubId = clubId;
        this.clubAdvisorMembers.add(clubAdmin);
        creatorCA.joinORCreateClub(this);
        this.clubAdmin=creatorCA;
    }
    //Constructor 2
    public Clubs(String clubId, String clubName, String clubType, String clubDescription) {
        this.clubName = clubName;
        this.clubId = clubId;
        this.clubType = clubType;
        this.clubDescription = clubDescription;
    }

    //Getters
    public String getClubId() {
        return clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public String getClubType() {
        return clubType;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public ClubAdvisor getClubAdmin(){
        return clubAdmin;
    }

    public ArrayList<ClubAdvisor> getClubAdvisorMembers(){
        return clubAdvisorMembers;
    }

    /**
     * Setting the club administrator of the club
     * @param clubAdmin Object of a ClubAdvisor class as
     */
    public void setClubAdmin(ClubAdvisor clubAdmin){
        if(clubAdvisorMembers.contains(clubAdmin)){
            this.clubAdmin=clubAdmin;
            clubAdmin.getClubsWithAdminAccess().add(this);
        }
    }

    /**
     * Adding the club advisor to the club
     * @param clubAdvisor Object of a ClubAdvisor class as
     */
    public void addClubAdvisor(ClubAdvisor clubAdvisor) {
        this.clubAdvisorMembers.add(clubAdvisor);
        clubAdvisor.joinORCreateClub(this);
    }

    /**
     * Removing the club advisor from the club
     * @param clubAdvisor Object of a  ClubAdvisor class as
     */
    public void removeClubAdvisor(ClubAdvisor clubAdvisor){
            this.clubAdvisorMembers.remove(clubAdvisor);
            clubAdvisor.leaveClub(this);
        }

    /**
     * Creating an event of a club
     * @param name Name of the club advisor who is going to organize the event : type String
     * @param eventName Name of the event going to organize : type String
     * @param eventDate Date of the event going to organize : type String
     * @param eventLocation Location of the event going to organize : type String
     * @param clubAdvisor Object of a ClubAdvisor class as
     */
    public void createEvent(String name, String eventName, String eventDate, String eventLocation, ClubAdvisor clubAdvisor){
        if (clubAdvisor!=null && clubAdvisor.equals(this.clubAdmin)){
            Events event=new Events(eventName,eventDate,eventLocation,this);
            this.clubEvents.add(event);
        }
    }

    /**
     * Suspending the selected event from the club
     * @param event Object of an Event class as
     */
    public void suspendEvent(Events event){
        this.clubEvents.remove(event);
    }


    /**
     * Marking the attendance of a student who attended an event
     * @param event Object of an Events class as
     * @param student Object of a Student class as
     */
    public void markAttendance(Events event, Student student){
        event.getAttendance().add(student);
    }
}
