package stake_holders;

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
    }

    //Validator methods
    //These methods will be used in constructor to check if the arguments given to constructor, follow the REGEX
    private void validateClubAdvisorId(String clubAdvisorId) {
        if (!clubAdvisorId.matches(CLUB_ADVISOR_ID_REGEX)) {
            throw new IllegalArgumentException("Invalid Club Advisor ID format: " + clubAdvisorId);
        }
    }

    private void validateName(String name) {
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid name format: " + name);
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
}
