package at.codersbay.courseapp.api.course.update;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import at.codersbay.courseapp.api.exceptions.InvalidUpdateException;
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
public class UpdateCourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private UpdateCourseService updateCourseService;

    // ---------- Helper ----------

    private Course createExistingCourse() {
        Course course = new Course("Java", "OOP",
                LocalDate.of(2026, 4, 30),
                LocalDate.of(2026, 2, 1),
                10);
        course.setId(1L);
        return course;
    }

    // ---------- Happy Path ----------

    @Test
    void update_validData_updatesCourseSuccessfully() {
        // Arrange
        Course existing = createExistingCourse();
        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any(Course.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO();
        dto.setId(1L);
        dto.setTitle("Java Advanced");
        dto.setDescription("Fortgeschrittene OOP");
        dto.setStartDate(LocalDate.of(2026, 5, 1));
        dto.setEndDate(LocalDate.of(2026, 7, 30));
        dto.setMaxParticipants(20);

        // Act
        Course result = updateCourseService.update(dto);

        // Assert
        assertThat(result.getTitle()).isEqualTo("Java Advanced");
        assertThat(result.getDescription()).isEqualTo("Fortgeschrittene OOP");
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2026, 7, 30));
        assertThat(result.getMaxParticipants()).isEqualTo(20);
        verify(courseRepository, times(1)).save(existing);
    }

    // ---------- Teilweise Updates ----------

    @Test
    void update_onlyTitle_keepsOtherFieldsUnchanged() {
        Course existing = createExistingCourse();
        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any(Course.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO();
        dto.setId(1L);
        dto.setTitle("Java Neu");

        Course result = updateCourseService.update(dto);

        assertThat(result.getTitle()).isEqualTo("Java Neu");
        assertThat(result.getDescription()).isEqualTo("OOP");
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2026, 2, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2026, 4, 30));
        assertThat(result.getMaxParticipants()).isEqualTo(10);
    }

    @Test
    void update_onlyDates_keepsOtherFieldsUnchanged() {
        Course existing = createExistingCourse();
        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any(Course.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO();
        dto.setId(1L);
        dto.setStartDate(LocalDate.of(2026, 6, 1));
        dto.setEndDate(LocalDate.of(2026, 8, 30));

        Course result = updateCourseService.update(dto);

        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2026, 6, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2026, 8, 30));
        assertThat(result.getTitle()).isEqualTo("Java");
        assertThat(result.getDescription()).isEqualTo("OOP");
    }

    // ---------- Fehlerfälle ----------

    @Test
    void update_courseNotFound_throwsException() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO();
        dto.setId(99L);
        dto.setTitle("Test");

        assertThrows(CourseNotFoundException.class,
                () -> updateCourseService.update(dto));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void update_nullDto_throwsException() {
        assertThrows(InvalidUpdateException.class,
                () -> updateCourseService.update(null));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void update_nullId_throwsException() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO();
        dto.setId(null);

        assertThrows(InvalidUpdateException.class,
                () -> updateCourseService.update(dto));

        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void update_invalidId_throwsException() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO();
        dto.setId(0L);

        assertThrows(InvalidUpdateException.class,
                () -> updateCourseService.update(dto));

        verify(courseRepository, never()).save(any(Course.class));
    }
}
