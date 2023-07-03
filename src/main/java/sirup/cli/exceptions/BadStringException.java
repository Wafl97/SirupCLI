package sirup.cli.exceptions;

public class BadStringException extends RuntimeException {
    public BadStringException() {
        super();
    }
    public BadStringException(String message) {
        super(message);
    }
}
