package at.codersbay.courseapp.api.course.get;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GetCourseControllerIntegrationTest {

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
    void getAll_returnsListOfCourses() throws Exception {
        mockMvc.perform(get("/api/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Testing Course"))
                .andExpect(jsonPath("$[0].description").value("A course for testing"))
                .andExpect(jsonPath("$[0].startDate").value(startDate.toString()))
                .andExpect(jsonPath("$[0].endDate").value(endDate.toString()))
                .andExpect(jsonPath("$[0].maxParticipants").value(10));
    }

    @Test
    void getById_existingCourse_returns200() throws Exception {
        mockMvc.perform(get("/api/course/" + savedCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Testing Course"));
    }

    @Test
    void getCourseById_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/api/course/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByTitle_existingCourse_returns200() throws Exception {
        mockMvc.perform(get("/api/course/byTitle/" + savedCourse.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Testing Course"));
    }

    @Test
    void getCourseByTitle_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/api/course/byTitle/Test"))
                .andExpect(status().isNotFound());
    }

}
