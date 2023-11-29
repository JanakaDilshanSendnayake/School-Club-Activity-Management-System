package stake_holders;

public  abstract class Users {
// Common REGEX
    private static final String ID_REGEX = "^[a-zA-Z0-9]{1,10}$";
    private static final String NAME_REGEX = "^[a-zA-Z_]{1,31}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@iit\\.ac\\.lk$";
    private static final String MOBILE_NUMBER_REGEX = "^\\d{10}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";

    // Common attributes
    private String name;
    private String email;
    private String mobileNumber;
    private String password;

    // Common constructor
    public Users(String name, String email, String mobileNumber, String password) {
        validateName(name);
        validateEmail(email);
        validateMobileNumber(mobileNumber);
        validatePassword(password);

        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
    }

    protected abstract void validateUserId(String userId);

    // Common validator methods
    private void validateName(String name) {
        if (name.endsWith("_")) {
            throw new IllegalArgumentException("You haven't filled the full name. It's mandatory");
        }
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid name format: " + name);
        }
    }

    private void validateEmail(String email) {
        if (email.isEmpty()) {
            throw new IllegalArgumentException("You haven't filled the email. It's mandatory");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    private void validateMobileNumber(String mobileNumber) {
        if (mobileNumber.isEmpty()) {
            throw new IllegalArgumentException("You haven't filled the mobile number. It's mandatory");
        }
        if (!mobileNumber.matches(MOBILE_NUMBER_REGEX)) {
            throw new IllegalArgumentException("Invalid mobile number format: " + mobileNumber);
        }
    }

    private void validatePassword(String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException("Invalid password format: " + password);
        }
    }

    // Getters and setters for common attributes
    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        validateMobileNumber(mobileNumber);
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
            validatePassword(password);
            this.password = password;
    }

}
