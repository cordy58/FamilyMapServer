package Result;

import Models.Person;

import java.util.List;

public class FindFamilyResult extends Result {
    private List<Person> data;

    /**
     * Creates a FindFamilyResult object
     * @param data List of Person objects
     * @param success Boolean that's true if succeeded, else false
     * @param error The message sent upon error
     */
    public FindFamilyResult(List<Person> data, boolean success, String error) {
        super(success, error);
        this.data = data;
    }

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }
}
