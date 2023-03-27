package Request;

import Models.User;
import Models.Person;
import Models.Event;

public class LoadRequest {
    private User[] users;
    private Person[] persons;
    private Event[] events;

    /**
     * Creates a LoadRequest object, which holds the information needed to execute a load request
     * @param userArray An array of User objects
     * @param personArray An array of Person objects
     * @param eventArray An array of Event objects
     */
    public LoadRequest(User[] userArray, Person[] personArray, Event[] eventArray) {
        this.users = userArray;
        this.persons = personArray;
        this.events = eventArray;
    }

    public User[] getUserArray() {
        return users;
    }

    public void setUserArray(User[] userArray) {
        this.users = userArray;
    }

    public Person[] getPersonArray() {
        return persons;
    }

    public void setPersonArray(Person[] personArray) {
        this.persons = personArray;
    }

    public Event[] getEventArray() {
        return events;
    }

    public void setEventArray(Event[] eventArray) {
        this.events = eventArray;
    }
}
