package Result;

public class LoginResult extends Result {
    private String authtoken;
    private String username;
    private String personID;

    /**
     * Creates a LoginResult object
     * @param success Boolean that's true if succeeded, else false
     * @param message The message sent upon completion
     * @param authToken The AuthToken used for validation
     * @param username The Username of the person trying to log in
     * @param personID The person ID of the person trying to log in
     */
    public LoginResult(boolean success, String message, String authToken, String username, String personID) {
        super(success, message);
        this.authtoken = authToken;
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
