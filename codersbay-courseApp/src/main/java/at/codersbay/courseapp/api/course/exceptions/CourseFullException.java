package at.codersbay.courseapp.api.course.exceptions;

public class CourseFullException extends RuntimeException {
    public CourseFullException(String message) {
        super(message);
    }
}
