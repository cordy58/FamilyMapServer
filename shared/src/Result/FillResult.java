package Result;

public class FillResult extends Result {

    /**
     * Creates a FillResult object
     * @param message The message sent upon completion
     * @param success Boolean that's true if succeeded, else false
     */
    public FillResult(String message, boolean success) {
        super(success, message);
    }
}
