package at.codersbay.courseapp.api.course.create;


import at.codersbay.courseapp.api.course.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CreateCourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
    }

    private LocalDate today = LocalDate.now();
    private LocalDate startDate = today.plusDays(10);
    private LocalDate endDate = today.plusDays(40);

    @Test
    void createCourse_validRequest_returns201() throws Exception {

        String json = """
            {
              "title": "Testing Course",
              "description": "A course for testing",
              "startDate": "%s",
              "endDate": "%s",
              "maxParticipants": 10
            }
            """.formatted(startDate, endDate);

        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Testing Course"))
                .andExpect(jsonPath("$.description").value("A course for testing"))
                .andExpect(jsonPath("$.startDate").value(startDate.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate.toString()))
                .andExpect(jsonPath("$.maxParticipants").value(10));
    }

    @Test
    void createCourse_missingTitle_returns400() throws Exception {

        String json = """
            {
              "description": "A course for testing",
              "startDate": "%s",
              "endDate": "%s",
              "maxParticipants": 10
            }
            """.formatted(startDate, endDate);

        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCourse_missingDescription_returns400() throws Exception {

        String json = """
            {
              "title": "Testing Course",
              "startDate": "%s",
              "endDate": "%s",
              "maxParticipants": 10
            }
            """.formatted(startDate, endDate);

        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCourse_missingStartDate_returns400() throws Exception {

        String json = """
            {
              "title": "Testing Course",
              "description": "A course for testing",
              "endDate": "%s",
              "maxParticipants": 10
            }
            """.formatted(startDate, endDate);

        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCourse_missingEndDate_returns400() throws Exception {

        String json = """
            {
              "title": "Testing Course",
              "description": "A course for testing",
              "startDate": "%s",
              "maxParticipants": 10
            }
            """.formatted(startDate, endDate);

        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCourse_missingMaxParticipants_returns400() throws Exception {

        String json = """
            {
              "title": "Testing Course",
              "description": "A course for testing",
              "startDate": "%s",
              "endDate": "%s"
            }
            """.formatted(startDate, endDate);

        mockMvc.perform(post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}
