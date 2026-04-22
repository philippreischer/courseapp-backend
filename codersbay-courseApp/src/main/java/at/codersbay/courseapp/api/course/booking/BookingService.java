package at.codersbay.courseapp.api.course.booking;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.exceptions.CourseFullException;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public Course handleBooking(BookingRequestDTO dto) {

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException(
                        "could not find course by id '" + dto.getCourseId() + "'."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(
                        "could not find user by id '" + dto.getUserId() + "'."));

        if (dto.getStatus() == BookingStatus.BOOK) {
            book(course, user);
        } else if (dto.getStatus() == BookingStatus.CANCEL) {
            cancel(course, user);
        }

        return course;
    }

    private void book(Course course, User user) {
        boolean alreadyBooked = course.getBookings().stream()
                .anyMatch(booking -> booking.getUser().getId() == user.getId()
                );
        if (alreadyBooked) {
            return;
        }

        if (course.getBookings().size() >= course.getMaxParticipants()) {
            throw new CourseFullException(
                    "course '" + course.getId() + "' is already full.");
        }

        Booking booking = new Booking(course, user, BookingStatus.BOOK);
        course.getBookings().add(booking);
        courseRepository.save(course);
    }

    private void cancel(Course course, User user) {
        course.getBookings().removeIf(
                booking -> booking.getUser().getId() == user.getId()
        );
        courseRepository.save(course);
    }
}
