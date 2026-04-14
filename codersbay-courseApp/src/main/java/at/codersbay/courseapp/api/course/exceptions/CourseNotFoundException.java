package at.codersbay.courseapp.api.course.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super(message);
    }
}
