package at.codersbay.courseapp.api.course.delete;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class DeleteCourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private DeleteCourseService deleteCourseService;

    @Test
    void deleteById_Course_existingId_deletesSuccessfully() {

        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course))
                .thenReturn(Optional.empty());

        deleteCourseService.deleteById(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_course_NotFound_throwsException() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class,
                () -> deleteCourseService.deleteById(99L));

        verify(courseRepository, never()).deleteById(anyLong());
    }
}
