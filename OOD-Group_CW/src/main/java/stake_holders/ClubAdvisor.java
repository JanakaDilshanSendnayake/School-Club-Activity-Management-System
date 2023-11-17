package stake_holders;

import java.util.ArrayList;

public class ClubAdvisor {
    private String clubAdvisorId;
    private String name;
    private String email ;
    private String mobileNum;
    private ArrayList<Clubs> advisedClubs;

    public ClubAdvisor(String clubAdvisorId, String name, String email, String mobileNum) {
        this.clubAdvisorId = clubAdvisorId;
        this.name = name;
        this.email = email;
        this.mobileNum = mobileNum;
    }
    public void joinORCreateClub(Clubs club) { //Do not call this method in driver seperately
        this.advisedClubs.add(club);
    }
    public void leaveClub(Clubs club){ //Do not call this method in driver seperately
        this.advisedClubs.remove(club);
    }
}
