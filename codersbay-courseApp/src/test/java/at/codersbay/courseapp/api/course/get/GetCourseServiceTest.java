package at.codersbay.courseapp.api.course.get;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.booking.Booking;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class GetCourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private GetCourseService getCourseService;

    private Course createCourse(String title, LocalDate start, LocalDate end) {
        Course course = new Course(title, "Beschreibung", start, end,  10);
        course.setBookings(new HashSet<>());
        return course;
    }

    @Test
    void getById_existingId_returnsCourse() {
        Course course = createCourse("Java",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 4, 30));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = getCourseService.getById(1L);

        assertThat(result.getTitle()).isEqualTo("Java");
    }

    @Test
    void getById_courseNotFound_throwsException() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class,
                () -> getCourseService.getById(99L));
    }

    @Test
    void getByTitle_existingTitle_returnsCourse() {
        Course course = createCourse("Java",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 4, 30));
        when(courseRepository.findByTitle("Java")).thenReturn(Optional.of(course));

        Course result = getCourseService.getByTitle("Java");

        assertThat(result.getTitle()).isEqualTo("Java");
    }

    @Test
    void getByTitle_titleNotFound_throwsException() {
        when(courseRepository.findByTitle("Unknown")).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class,
                () -> getCourseService.getByTitle("Unknown"));
    }

    @Test
    void getAll_multipleCourses_returnsList() {
        Course course1 = createCourse("Java",
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 4, 30));
        Course course2 = createCourse("Python",
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 7, 30));
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = getCourseService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Java");
        assertThat(result.get(1).getTitle()).isEqualTo("Python");
    }

    @Test
    void getAll_noCourses_returnsEmptyList() {
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        List<Course> result = getCourseService.getAll();

        assertThat(result).isEmpty();
    }

    @Test
    void getAllBooked_onlyCoursesWithBookings_returned() {
        Course withBookings = createCourse("Java",
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 4, 30));
        Set<Booking> bookings = new HashSet<>();
        bookings.add(mock(Booking.class));  // ← Mockito-Mock
        withBookings.setBookings(bookings);

        Course withoutBookings = createCourse("Python",
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 7, 30));

        when(courseRepository.findAll())
                .thenReturn(Arrays.asList(withBookings, withoutBookings));

        List<Course> result = getCourseService.getAllBooked();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Java");
    }

    @Test
    void getAllBooked_noCoursesWithBookings_returnsEmptyList() {
        Course course1 = createCourse("Java",
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 4, 30));
        Course course2 = createCourse("Python",
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 7, 30));

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = getCourseService.getAllBooked();

        assertThat(result).isEmpty();
    }

    @Test
    void getAllCurrentDate_courseWithinRange_returned() {
        LocalDate today = LocalDate.now();

        Course currentCourse = createCourse("Java",
                today.minusDays(10), today.plusDays(10));
        Course pastCourse = createCourse("Python",
                today.minusDays(60), today.minusDays(30));
        Course futureCourse = createCourse("HTML",
                today.plusDays(30), today.plusDays(60));

        when(courseRepository.findAll())
                .thenReturn(Arrays.asList(currentCourse, pastCourse, futureCourse));

        List<Course> result = getCourseService.getAllCurrentDate();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Java");
    }

    @Test
    void getAllCurrentDate_noCoursesInRange_returnsEmptyList() {
        LocalDate today = LocalDate.now();

        Course pastCourse = createCourse("Python",
                today.minusDays(60), today.minusDays(30));

        when(courseRepository.findAll())
                .thenReturn(Collections.singletonList(pastCourse));

        List<Course> result = getCourseService.getAllCurrentDate();

        assertThat(result).isEmpty();
    }
}