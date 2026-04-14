package at.codersbay.courseapp.api.course.exceptions;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException(String message) {
        super(message);
    }
}
