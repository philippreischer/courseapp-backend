package at.codersbay.courseapp.api.course.booking;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.exceptions.CourseFullException;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import at.codersbay.courseapp.api.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    private Course course;
    private User user;

    @BeforeEach
    void setUp() {
        course = new Course("Java", "OOP",
                LocalDate.of(2026, 4, 30),
                LocalDate.of(2026, 2, 1),
                2);
        course.setId(1L);
        course.setBookings(new HashSet<>());

        user = new User("Maxi", "Max", "Mustermann",
                "max@mustermann.com", "pass123");
        user.setId(10L);
    }

    @Test
    void handleBooking_book_addsBookingToCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setCourseId(1L);
        dto.setUserId(10L);
        dto.setStatus(BookingStatus.BOOK);

        Course result = bookingService.handleBooking(dto);

        assertThat(result.getBookings()).hasSize(1);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void handleBooking_book_alreadyBooked_doesNotAddAgain() {

        course.getBookings().add(new Booking(course, user, BookingStatus.BOOK));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setCourseId(1L);
        dto.setUserId(10L);
        dto.setStatus(BookingStatus.BOOK);

        Course result = bookingService.handleBooking(dto);

        assertThat(result.getBookings()).hasSize(1);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void handleBooking_book_courseFull_throwsException() {

        User otherUser1 = new User("Anna", "Anna", "Beispiel",
                "anna@example.com", "pass");
        otherUser1.setId(20L);
        User otherUser2 = new User("Bob", "Bob", "Builder",
                "bob@example.com", "pass");
        otherUser2.setId(30L);

        course.getBookings().add(new Booking(course, otherUser1, BookingStatus.BOOK));
        course.getBookings().add(new Booking(course, otherUser2, BookingStatus.BOOK));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setCourseId(1L);
        dto.setUserId(10L);
        dto.setStatus(BookingStatus.BOOK);

        assertThrows(CourseFullException.class,
                () -> bookingService.handleBooking(dto));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void handleBooking_cancel_removesBookingFromCourse() {
        course.getBookings().add(new Booking(course, user, BookingStatus.BOOK));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setCourseId(1L);
        dto.setUserId(10L);
        dto.setStatus(BookingStatus.CANCEL);

        Course result = bookingService.handleBooking(dto);

        assertThat(result.getBookings()).isEmpty();
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void handleBooking_cancel_userNotBooked_doesNothing() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setCourseId(1L);
        dto.setUserId(10L);
        dto.setStatus(BookingStatus.CANCEL);

        Course result = bookingService.handleBooking(dto);

        assertThat(result.getBookings()).isEmpty();
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void handleBooking_courseNotFound_throwsException() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setCourseId(99L);
        dto.setUserId(10L);
        dto.setStatus(BookingStatus.BOOK);

        assertThrows(CourseNotFoundException.class,
                () -> bookingService.handleBooking(dto));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void handleBooking_userNotFound_throwsException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setCourseId(1L);
        dto.setUserId(99L);
        dto.setStatus(BookingStatus.BOOK);

        assertThrows(UserNotFoundException.class,
                () -> bookingService.handleBooking(dto));

        verify(courseRepository, never()).save(any(Course.class));
    }
}
