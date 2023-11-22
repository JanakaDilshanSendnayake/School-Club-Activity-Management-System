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

    //regex
    private static final String CLUB_NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String CLUB_DESCRIPTION_REGEX = "^.{1,200}$";

    public Clubs(String clubName, String clubId, ClubAdvisor creatorCA) {
        clubAdvisorMembers=new ArrayList<>();
        this.clubName = clubName;
        this.clubId = clubId;
        this.clubAdvisorMembers.add(clubAdmin);
        creatorCA.joinORCreateClub(this);
        this.clubAdmin=creatorCA;
    }
    //Constructor 2. This will be used to create a club object when registering a new club, and then that-
    // -object will be passed into database. Here club id won't be taken as an argument because-
    //-club id is automatically generated using auto increment in the database.
    public Clubs(String clubName, String clubType, String clubDescription,ClubAdvisor creator) {
        validateClubName(clubName);
        validateClubType(clubType);
        validateClubDescription(clubDescription);

        this.clubName = clubName;
        this.clubType = clubType;
        this.clubDescription = clubDescription;
        this.clubAdmin=creator;
    }
    //This will be used when loading clubs from the database to the system. Data from relevant columns will be given-
    // -as arguments to this constructor and then the created object will be saved in arrays for future uses.
    public Clubs(String clubId, String clubName, String clubType, String clubDescription) {
        this.clubId=clubId;
        this.clubName = clubName;
        this.clubType = clubType;
        this.clubDescription = clubDescription;
    }

    private void validateClubName(String name){
        if(name.equals("")){
            throw new IllegalArgumentException("You haven't filled club name. It's mandatory");
        }else{
            if(!name.matches(CLUB_NAME_REGEX)){
                throw new IllegalArgumentException("Invalid name format: " + name);
            }
        }
    }
    private void validateClubDescription(String description){
        if(description.equals("")){

        }else{
            if(!description.matches(CLUB_DESCRIPTION_REGEX)){
                throw new IllegalArgumentException("You have exceeded the maximum character limit.");
            }
        }
    }
    private void validateClubType(String type){
        if(type==null){
            throw new IllegalArgumentException("You haven't selected a club type");
        }
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
    public ArrayList<ClubAdvisor> getClubAdvisorMembers(){ return clubAdvisorMembers; }

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
