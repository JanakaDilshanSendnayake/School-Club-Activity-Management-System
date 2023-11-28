package stake_holders;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.ArrayList;

public class Clubs {

    private String clubName;
    private String clubId;
    private String clubType;
    private String clubDescription;
    private ArrayList<ClubAdvisor> clubAdmin;
    private ArrayList<ClubAdvisor> clubAdvisorMembers;
    private ArrayList<Student> studentMembers;
    private ArrayList<Events> clubEvents;

    //regex
    private static final String CLUB_NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String CLUB_DESCRIPTION_REGEX = "^.{1,500}$";

    //Constructor 2. This will be used to create a club object when registering a new club, and then that-
    // -object will be passed into database. Here club id won't be taken as an argument because-
    //-club id is automatically generated using auto increment in the database.
    public Clubs(String clubId, String clubName, String clubType, String clubDescription,ClubAdvisor creator) {
        validateClubName(clubName);
        validateClubType(clubType);
        validateClubDescription(clubDescription);

        clubAdvisorMembers=new ArrayList<>();
        studentMembers=new ArrayList<>();
        clubEvents=new ArrayList<>();
        clubAdmin=new ArrayList<>();


        this.clubId=clubId;
        this.clubName = clubName;
        this.clubType = clubType;
        this.clubDescription = clubDescription;
        this.clubAdmin.add(creator);
    }
    //This will be used when loading clubs from the database to the system. Data from relevant columns will be given-
    // -as arguments to this constructor and then the created object will be saved in arrays for future uses.
    public Clubs(String clubId, String clubName, String clubType, String clubDescription) {
        this.clubId=clubId;
        this.clubName = clubName;
        this.clubType = clubType;
        this.clubDescription = clubDescription;

        clubAdvisorMembers=new ArrayList<>();
        studentMembers=new ArrayList<>();
        clubEvents=new ArrayList<>();
        clubAdmin=new ArrayList<>();
    }

    private void validateClubName(String name){
        if(name.isEmpty()) {
            throw new IllegalArgumentException("You haven't filled club name. It's mandatory");
        }
        if(!name.matches(CLUB_NAME_REGEX)){
            throw new IllegalArgumentException("Invalid name format: " + name);
        }
    }
    private void validateClubDescription(String description){
        if(description.isEmpty()){
            throw new IllegalArgumentException("You haven't filled club description. It's mandatory");
        }
        if(!description.matches(CLUB_DESCRIPTION_REGEX)){
            throw new IllegalArgumentException("You have exceeded the maximum character limit.");
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

    public ArrayList<ClubAdvisor> getClubAdmin(){
        return clubAdmin;
    }
    public ArrayList<ClubAdvisor> getClubAdvisorMembers(){ return clubAdvisorMembers; }
    public ArrayList<Student> getStudentMembers(){return studentMembers;}

    public void setClubAdmin(ArrayList<ClubAdvisor> clubAdmin){
        this.clubAdmin=clubAdmin;
    }

    public void setClubAdvisorMembers(ArrayList<ClubAdvisor> clubAdvisorMembers) {
        this.clubAdvisorMembers = clubAdvisorMembers;
    }

    public void setStudentMembers(ArrayList<Student> studentMembers) {
        this.studentMembers = studentMembers;
    }

    public ArrayList<Events> getClubEvents() {
        return clubEvents;
    }

    public void setClubEvents(ArrayList<Events> clubEvents) {
        this.clubEvents = clubEvents;
    }

    public Events createEvent(String eventId, String eventName, LocalDate eventDate, String eventLocation, String eventDescription){

        return new Events(eventId,eventName,eventDate,eventLocation,eventDescription, this);
            //this.clubEvents.add(event);

    }
    public void suspendEvent(Events event){
        this.clubEvents.remove(event);
    }

}
