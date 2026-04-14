package at.codersbay.courseapp.api.exceptions;

import at.codersbay.courseapp.api.course.exceptions.*;
import at.codersbay.courseapp.api.response.ResponseBody;
import at.codersbay.courseapp.api.user.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ---------- 404 Not Found ----------

    @ExceptionHandler({UserNotFoundException.class, CourseNotFoundException.class})
    public ResponseEntity<ResponseBody> handleNotFound(RuntimeException e) {
        ResponseBody body = new ResponseBody();
        body.addErrorMessage(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // ---------- 400 Bad Request ----------

    @ExceptionHandler(InvalidUpdateException.class)
    public ResponseEntity<ResponseBody> handleBadRequest(RuntimeException e) {
        ResponseBody body = new ResponseBody();
        body.addErrorMessage(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ---------- 409 Conflict ----------

    @ExceptionHandler({UserAlreadyExistsException.class, CourseFullException.class})
    public ResponseEntity<ResponseBody> handleConflict(RuntimeException e) {
        ResponseBody body = new ResponseBody();
        body.addErrorMessage(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // ---------- 500 Internal Server Error ----------

    @ExceptionHandler({UserDeletionException.class, CourseDeletionException.class})
    public ResponseEntity<ResponseBody> handleServerError(RuntimeException e) {
        ResponseBody body = new ResponseBody();
        body.addErrorMessage(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ---------- Fallback für alle anderen Exceptions ----------

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseBody> handleUnknown(Throwable t) {
        ResponseBody body = new ResponseBody();
        body.addErrorMessage("An unexpected error has occurred.");
        body.addErrorMessage(t.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
