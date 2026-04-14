package at.codersbay.courseapp.api.exceptions;

public class InvalidUpdateException extends RuntimeException {
    public InvalidUpdateException(String message) {
        super(message);
    }
}
