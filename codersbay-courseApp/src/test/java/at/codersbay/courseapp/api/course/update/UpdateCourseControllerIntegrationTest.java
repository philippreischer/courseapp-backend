package at.codersbay.courseapp.api.course.update;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UpdateCourseControllerIntegrationTest {

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
    void update_validData_returns200() throws Exception {
        String json = """
            {
              "id": %d,
              "title": "New Title",
              "description": "New Description",
              "startDate": "%s",
              "endDate": "%s",
              "maxParticipants": 20
            }
            """.formatted(savedCourse.getId(), startDate, endDate);

        mockMvc.perform(put("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.maxParticipants").value(20));
    }

    @Test
    void update_onlyTitle_keepsOtherFieldsUnchanged_returns200() throws Exception {
        String json = """
            {
              "id": %d,
              "title": "Nur Titel neu"
            }
            """.formatted(savedCourse.getId());

        mockMvc.perform(put("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Nur Titel neu"))
                .andExpect(jsonPath("$.description").value("A course for testing"))
                .andExpect(jsonPath("$.startDate").value(startDate.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate.toString()))
                .andExpect(jsonPath("$.maxParticipants").value(10));
    }

    @Test
    void update_onlyDates_keepsOtherFieldsUnchanged_returns200() throws Exception {
        LocalDate newStart = today.plusDays(20);
        LocalDate newEnd = today.plusDays(50);

        String json = """
            {
              "id": %d,
              "startDate": "%s",
              "endDate": "%s"
            }
            """.formatted(savedCourse.getId(), newStart, newEnd);

        mockMvc.perform(put("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Testing Course"))
                .andExpect(jsonPath("$.startDate").value(newStart.toString()))
                .andExpect(jsonPath("$.endDate").value(newEnd.toString()));
    }


    @Test
    void update_unknownId_returns404() throws Exception {
        String json = """
            {
              "id": 9999,
              "title": "Test"
            }
            """;

        mockMvc.perform(put("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_changesPersistedInDatabase_returns200() throws Exception {
        String json = """
            {
              "id": %d,
              "title": "Persistenz Test"
            }
            """.formatted(savedCourse.getId());

        mockMvc.perform(put("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/course/" + savedCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Persistenz Test")).andDo(print());
    }
}
