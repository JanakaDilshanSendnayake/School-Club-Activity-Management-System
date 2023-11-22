package stake_holders;

import java.security.PublicKey;
import java.util.ArrayList;

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

    public void setClubAdmin(ClubAdvisor clubAdmin){
        if(clubAdvisorMembers.contains(clubAdmin)){
            this.clubAdmin=clubAdmin;
            clubAdmin.getClubsWithAdminAccess().add(this);
        }
    }

    public void addClubAdvisor(ClubAdvisor clubAdvisor) { //****
        this.clubAdvisorMembers.add(clubAdvisor);
        clubAdvisor.joinORCreateClub(this);
    }

    public void removeClubAdvisor(ClubAdvisor clubAdvisor){ //****
        if (clubAdvisor!=null && !clubAdvisor.equals(this.clubAdmin)){
            this.clubAdvisorMembers.remove(clubAdvisor);
            clubAdvisor.leaveClub(this);
        }
    }
    public ArrayList<ClubAdvisor> getClubAdvisorMembers(){ return clubAdvisorMembers; }
    public void createEvent(String name, String eventName, String eventDate, String eventLocation, ClubAdvisor clubAdvisor){
        if (clubAdvisor!=null && clubAdvisor.equals(this.clubAdmin)){
            Events event=new Events(eventName,eventDate,eventLocation,this);
            this.clubEvents.add(event);
        }
    }
    public void suspendEvent(Events event){
        this.clubEvents.remove(event);
    }

    public void markAttendance(Events event, Student student){
        event.getAttendance().add(student);
    }
}
