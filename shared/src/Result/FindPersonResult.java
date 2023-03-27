package Result;

public class FindPersonResult extends Result {
    String associatedUsername = null;
    String personID = null;
    String firstName = null;
    String lastName = null;
    String gender = null;
    String fatherID = null;
    String motherID = null;
    String spouseID = null;

    /**
     * creates a FindEventResult object
     * @param associatedUsername The username associated with the event
     * @param personID The personID associated with the event
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     * @param gender The gender of the person
     * @param fatherID The unique ID of the person's father
     * @param motherID The unique ID of the person's mother
     * @param spouseID The unique ID of the person's spouse
     * @param success Boolean that's true if succeeded, else false
     * @param message The message sent upon error
     */
    public FindPersonResult(String associatedUsername, String personID, String firstName, String lastName, String gender,
                            String fatherID, String motherID, String spouseID, boolean success, String message) {
        super(success, message);
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
