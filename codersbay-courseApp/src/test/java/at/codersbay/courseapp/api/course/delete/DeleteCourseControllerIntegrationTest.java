package at.codersbay.courseapp.api.course.delete;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class DeleteCourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    private Course savedCourse;

    private LocalDate today = LocalDate.now();
    private LocalDate startDate = today.plusDays(10);
    private LocalDate endDate = today.plusDays(40);

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        Course course = new Course("Testing Course", "A course for testing",
                startDate, endDate, 10);
        savedCourse = courseRepository.save(course);
    }

    @Test
    void deleteById_existingCourse_returns202() throws Exception {

        mockMvc.perform(delete("/api/course/" + savedCourse.getId()))
                .andExpect(status().isAccepted());
    }

    @Test
    void deleteById_unknownCourseId_returns404() throws Exception {

        mockMvc.perform(delete("/api/course/9999"))
                .andExpect(status().isNotFound());
    }

}
