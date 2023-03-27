package Result;

public class LoadResult extends Result {

    /**
     * Creates a LoadResult object
     * @param message The message sent upon success
     * @param success Boolean that's true if succeeded, else false
     */
    public LoadResult(String message, boolean success) {
        super(success, message);
    }
}
