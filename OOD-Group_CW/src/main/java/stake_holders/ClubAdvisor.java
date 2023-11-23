package stake_holders;

import java.util.ArrayList;

/**
 * Class which holds variables and methods of club advisor
 */
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


    // Below methods will be used in constructor to check if the arguments given to constructor, follow the REGEX


    /**
     * Validator method to validate the clubAdvisorID
     * @param clubAdvisorId Unique identifier used to identify the club advisor : type String
     */
    private void validateClubAdvisorId(String clubAdvisorId) {
        if (!clubAdvisorId.matches(CLUB_ADVISOR_ID_REGEX)) {
            throw new IllegalArgumentException("Invalid Club Advisor ID format: " + clubAdvisorId);
        }
    }


    /**
     * Validator method to validate the name of the club advisor
     * @param name Name of the club advisor : type String
     */
    private void validateName(String name) {
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid name format: " + name);
        }
    }


    /**
     * Validator method to validate the email of the club advisor
     * @param email Email of the club advisor : type String
     */
    private void validateEmail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }


    /**
     * Validator method to validate the mobile number of the club advisor
     * @param mobileNum Mobile number of the club advisor : type String
     */
    private void validateMobileNumber(String mobileNum) {
        if (!mobileNum.matches(MOBILE_NUMBER_REGEX)) {
            throw new IllegalArgumentException("Invalid mobile number format: " + mobileNum);
        }
    }


    /**
     * Validator method to validate the password of the club advisor account
     * @param password Password of the club advisor account : type String
     */
    private void validatePassword(String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException("Invalid password format: " + password);
        }
    }


    /**
     * Club advisor joining or creating club method
     * @param club Object of Club class as
     * Do not call this method in driver separately
     */
    public void joinORCreateClub(Clubs club) {
        this.clubsWithAdminAccess.add(club);
    }


    /**
     * Club advisor leaving the club method
     * @param club Object of Club class as
     * Do not call this method in driver separately
     */
    public void leaveClub(Clubs club){
        this.clubsWithoutAdminAccess.remove(club);
    }

}
