package Result;

public class Result {
    private boolean success = false;
    private String message;

    /**
     * Creates a Result object
     * @param success Boolean that's true if succeeded, else false
     * @param message The message returned
     */
    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
