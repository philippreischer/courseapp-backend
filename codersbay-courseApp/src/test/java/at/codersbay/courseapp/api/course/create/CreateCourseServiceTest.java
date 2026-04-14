package at.codersbay.courseapp.api.course.create;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.exceptions.CourseAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CreateCourseService createCourseService;

    @Test
    void createCourse_validInput_returnsCreatedCourse() {
        // Arrange
        LocalDate startDate = LocalDate.of(2026, 2, 16);
        LocalDate endDate = LocalDate.of(2026, 4, 15);

        when(courseRepository.findByTitle("Test Course")).thenReturn(Optional.empty());

        Course savedCourse = new Course("Test Course", "Test Course description ",
                startDate, endDate, 10);
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // Act
        Course result = createCourseService.createCourse(
                "Test Course", "Test Course description ", startDate,
                endDate, 10);

        // Assert
        assertThat(result.getTitle()).isEqualTo("Test Course");
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void createCourse_courseTitleExists_throwsException() {

        LocalDate startDate = LocalDate.of(2026, 2, 16);
        LocalDate endDate = LocalDate.of(2026, 4, 15);

        Course existing = new Course();

        when(courseRepository.findByTitle("Test Course")).thenReturn(Optional.of(existing));

        assertThrows(CourseAlreadyExistsException.class, () ->
                createCourseService.createCourse("Test Course", "Test Course description ", startDate,
                        endDate, 10)
        );

        verify(courseRepository, never()).save(any(Course.class));
    }

}
