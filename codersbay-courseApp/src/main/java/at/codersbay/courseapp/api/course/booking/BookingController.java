package at.codersbay.courseapp.api.course.booking;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import at.codersbay.courseapp.api.course.exceptions.CourseFullException;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course/")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PatchMapping("/booking")
    public ResponseEntity<CourseResponseBody> booking(
            @RequestBody BookingRequestDTO bookingRequestDTO) {

        if (bookingRequestDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Course course = bookingService.handleBooking(bookingRequestDTO);
            return new ResponseEntity<>(new CourseResponseBody(course), HttpStatus.OK);

        } catch (CourseNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (CourseFullException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}