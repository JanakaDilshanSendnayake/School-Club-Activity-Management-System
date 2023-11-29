package stake_holders;

import javafx.fxml.FXML;
import utils.CADataHandling;

import java.util.ArrayList;

public class ClubAdvisor extends Users {

    //REGEX
    private static final String CLUB_ADVISOR_ID_REGEX = "^[a-zA-Z0-9]{1,10}$";
    private String clubAdvisorId;
    private ArrayList<Clubs> clubsWithoutAdminAccess;
    private ArrayList<Clubs> clubsWithAdminAccess;

    //This constructor will be used to validate the user inputs when registering a new club advisor

    public ClubAdvisor(String clubAdvisorId, String name, String email, String mobileNum, String password) {
        super(name, email, mobileNum, password); // Call the constructor of the parent class
        validateUserId(clubAdvisorId);

        this.clubAdvisorId = clubAdvisorId;
        clubsWithoutAdminAccess = new ArrayList<>();
        clubsWithAdminAccess = new ArrayList<>();
    }
    // Constructor for loading data from the database
    public ClubAdvisor(String name, String email, String mobileNum, String password) {
        super(name, email, mobileNum, password);

        clubsWithoutAdminAccess = new ArrayList<>();
        clubsWithAdminAccess = new ArrayList<>();
    }
    //This setter will be used alongside with about method to bypass id validation for already existing ids in database.
    public void setClubAdvisorId(String clubAdvisorId) {
        this.clubAdvisorId = clubAdvisorId;
    }


    //Validator methods
    //These methods will be used in constructor to check if the arguments given to constructor, follow the conditions
    @FXML
    protected void validateUserId(String clubAdvisorId) {
        CADataHandling clubAdvisor=new CADataHandling();
       if (clubAdvisorId.isEmpty()) {
            throw new IllegalArgumentException("Club advisor ID is mandatory and cannot be empty");
        }
        if (!clubAdvisorId.matches(CLUB_ADVISOR_ID_REGEX)) {
            throw new IllegalArgumentException("Invalid Club Advisor ID format: " + clubAdvisorId);
        }
        if (clubAdvisor.clubAdvisorUserNameValidation(clubAdvisorId)) {
            throw new IllegalArgumentException("This username is already being used.");
        }
    }


    public void joinORCreateClub(Clubs club) { //Do not call this method in driver seperately
        this.clubsWithAdminAccess.add(club);
    }
    public void leaveClub(Clubs club){ //Do not call this method in driver seperately
        this.clubsWithoutAdminAccess.remove(club);
    }

    public String getClubAdvisorId() {
        return clubAdvisorId;
    }

    public ArrayList<Clubs> getClubsWithoutAdminAccess() {
        return clubsWithoutAdminAccess;
    }

    public ArrayList<Clubs> getClubsWithAdminAccess() {
        return clubsWithAdminAccess;
    }

    public void setClubsWithoutAdminAccess(ArrayList<Clubs> clubsWithoutAdminAccess) {
        this.clubsWithoutAdminAccess = clubsWithoutAdminAccess;
    }

    public void setClubsWithAdminAccess(ArrayList<Clubs> clubsWithAdminAccess) {
        this.clubsWithAdminAccess = clubsWithAdminAccess;
    }
}
