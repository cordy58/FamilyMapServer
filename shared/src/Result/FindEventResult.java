package Result;

public class FindEventResult extends Result {
    String associatedUsername = null;
    String eventID = null;
    String personID = null;
    double latitude = 0.0;
    double longitude = 0.0;
    String country = null;
    String city = null;
    String eventType = null;
    int year = 0;

    /**
     * creates a FindEventResult object
     * @param associatedUsername The username associated with the event
     * @param eventID The eventID associated with the event
     * @param personID The personID associated with the event
     * @param latitude The latitude associated with the event
     * @param longitude The longitude associated with the event
     * @param country The country associated with the event
     * @param city The city associated with the event
     * @param eventType The eventType associated with the event
     * @param year The year associated with the event
     * @param success Boolean that's true if succeeded, else false
     * @param message The message sent upon error
     */
    public FindEventResult(String associatedUsername, String eventID, String personID, double latitude, double longitude,
                           String country, String city, String eventType, int year, boolean success, String message) {
        super(success, message);
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
