public class EmptyDescriptionException extends DukeException {
    private static final String ERROR_MESSAGE = "I cannot create a task without a description!";
    public EmptyDescriptionException() {
        super(ERROR_MESSAGE);
    }
}