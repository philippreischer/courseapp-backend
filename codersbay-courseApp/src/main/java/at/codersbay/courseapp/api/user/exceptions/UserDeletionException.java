package at.codersbay.courseapp.api.user.exceptions;

public class UserDeletionException extends RuntimeException {
    public UserDeletionException(String message) {
        super(message);
    }
}
