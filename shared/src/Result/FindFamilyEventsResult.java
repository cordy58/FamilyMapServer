package Result;

import Models.Event;

import java.util.List;


public class FindFamilyEventsResult extends Result {
    private List<Event> data;

    /**
     * Creates a FindFamilyEventsResult object
     * @param data List of event objects
     * @param success Boolean that's true if succeeded, else false
     * @param error The message sent upon error
     */
    public FindFamilyEventsResult(List<Event> data, boolean success, String error) {
        super(success, error);
        this.data = data;
    }

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }
}
