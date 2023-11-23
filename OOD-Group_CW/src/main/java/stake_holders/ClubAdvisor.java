package stake_holders;

import utils.CADataHandling;

import java.util.ArrayList;

public class ClubAdvisor {

    //REGEX
    private static final String CLUB_ADVISOR_ID_REGEX = "^[a-zA-Z0-9]{1,10}$";
    private static final String NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@iit\\.ac\\.lk$";
    private static final String MOBILE_NUMBER_REGEX = "^\\d{10}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";
    private String clubAdvisorId;
    private String name;
    private String email ;
    private String mobileNum;
    private String password;
    private ArrayList<Clubs> clubsWithoutAdminAccess;
    private ArrayList<Clubs> clubsWithAdminAccess;

    //This constructor will be used to validate the user inputs when registering a new club advisor

    public ClubAdvisor(String clubAdvisorId, String name, String email, String mobileNum, String password) {
        validateClubAdvisorId(clubAdvisorId);
        validateName(name);
        validateEmail(email);
        validateMobileNumber(mobileNum);
        validatePassword(password);

        this.clubAdvisorId = clubAdvisorId;
        this.name = name;
        this.email = email;
        this.mobileNum = mobileNum;
        this.password=password;

        clubsWithoutAdminAccess=new ArrayList<>();
        clubsWithAdminAccess=new ArrayList<>();
    }

    //This constructor will be used alone side with ClubAdvisorId when loading data from database to system
    public ClubAdvisor(String name, String email, String mobileNum, String password) {
        this.name = name;
        this.email = email;
        this.mobileNum = mobileNum;
        this.password=password;

        clubsWithoutAdminAccess=new ArrayList<>();
        clubsWithAdminAccess=new ArrayList<>();
    }
    //This setter will be used only
    public void setClubAdvisorId(String clubAdvisorId) {
        this.clubAdvisorId = clubAdvisorId;
    }


    //Validator methods
    //These methods will be used in constructor to check if the arguments given to constructor, follow the REGEX
    private void validateClubAdvisorId(String clubAdvisorId) {
        CADataHandling clubAdvisor=new CADataHandling();
//        if (!clubAdvisorId.matches(CLUB_ADVISOR_ID_REGEX)&&clubAdvisor.clubAdvisorUserNameValidation(clubAdvisorId)) {
//            throw new IllegalArgumentException("Invalid Club Advisor ID format: " + clubAdvisorId);
//        }
        if(clubAdvisorId.equals("")){
            throw new IllegalArgumentException("You haven't filled club advisor id. It's mandatory");
        }else{
            if(clubAdvisorId.matches(CLUB_ADVISOR_ID_REGEX)){
                if(clubAdvisor.clubAdvisorUserNameValidation(clubAdvisorId)){
                    throw new IllegalArgumentException("This username is already being used.");
                }
            }else{
                throw new IllegalArgumentException("Invalid Club Advisor ID format: " + clubAdvisorId);
            }
        }
    }

    private void validateName(String name) {
        if(name.equals("_")){
            throw new IllegalArgumentException("You haven't filled club advisor name. It's mandatory");
        }else{
            if(!name.matches(NAME_REGEX)){
                throw new IllegalArgumentException("Invalid name format: " + name);
            }
        }
    }

    private void validateEmail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    private void validateMobileNumber(String mobileNum) {
        if (!mobileNum.matches(MOBILE_NUMBER_REGEX)) {
            throw new IllegalArgumentException("Invalid mobile number format: " + mobileNum);
        }
    }

    private void validatePassword(String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException("Invalid password format: " + password);
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

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Clubs> getClubsWithoutAdminAccess() {
        return clubsWithoutAdminAccess;
    }

    public ArrayList<Clubs> getClubsWithAdminAccess() {
        return clubsWithAdminAccess;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClubsWithoutAdminAccess(ArrayList<Clubs> clubsWithoutAdminAccess) {
        this.clubsWithoutAdminAccess = clubsWithoutAdminAccess;
    }

    public void setClubsWithAdminAccess(ArrayList<Clubs> clubsWithAdminAccess) {
        this.clubsWithAdminAccess = clubsWithAdminAccess;
    }
}
