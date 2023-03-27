package Result;

public class RegisterResult extends Result {
    private String authtoken = null;
    private String username = null;
    private String personID = null;

    /**
     * Creates a RegisterResult object
     * @param authtoken The AuthToken used for validation
     * @param username The new user's username
     * @param personID The new user's personID
     * @param success Boolean that's true if succeeded, else false
     * @param error The message sent upon error
     */
    public RegisterResult(String authtoken, String username, String personID, String error, boolean success) {
        super(success, error);
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
