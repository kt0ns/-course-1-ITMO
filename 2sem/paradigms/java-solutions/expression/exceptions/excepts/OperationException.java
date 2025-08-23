package expression.exceptions.excepts;

public class OperationException extends RuntimeException {
    public OperationException(String ErrorMessage) {
        super(ErrorMessage);
    }
}
