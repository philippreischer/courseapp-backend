package at.codersbay.courseapp.api.course.booking;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    private User testUser;
    private Course testCourse;

    @BeforeEach
    void setUp() {

        testUser = new User("maxmuster", "Max", "Muster", "test@test.com", "password123");
        testUser = userRepository.save(testUser);

        testCourse = new Course(
                "Spring Boot Kurs",
                "Eine Kursbeschreibung",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(40),
                10
        );
        testCourse = courseRepository.save(testCourse);
    }

    @Test
    void booking_validBook_shouldReturn200() throws Exception {
        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setUserId(testUser.getId());
        dto.setCourseId(testCourse.getId());
        dto.setStatus(BookingStatus.BOOK);

        mockMvc.perform(patch("/api/course/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCourse.getId()))
                .andExpect(jsonPath("$.title").value("Spring Boot Kurs"));
    }

    @Test
    void booking_validCancel_shouldReturn200() throws Exception {

        BookingRequestDTO bookDto = new BookingRequestDTO();
        bookDto.setUserId(testUser.getId());
        bookDto.setCourseId(testCourse.getId());
        bookDto.setStatus(BookingStatus.BOOK);

        mockMvc.perform(patch("/api/course/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk());

        BookingRequestDTO cancelDto = new BookingRequestDTO();
        cancelDto.setUserId(testUser.getId());
        cancelDto.setCourseId(testCourse.getId());
        cancelDto.setStatus(BookingStatus.CANCEL);

        mockMvc.perform(patch("/api/course/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelDto)))
                .andExpect(status().isOk());
    }

    @Test
    void booking_alreadyBooked_shouldSilentlyIgnoreAndReturn200() throws Exception {
        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setUserId(testUser.getId());
        dto.setCourseId(testCourse.getId());
        dto.setStatus(BookingStatus.BOOK);

        mockMvc.perform(patch("/api/course/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/course/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void booking_nullBody_shouldReturn400() throws Exception {
        mockMvc.perform(patch("/api/course/booking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    void booking_userNotFound_shouldReturn400() throws Exception {
        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setUserId(9999L);
        dto.setCourseId(testCourse.getId());
        dto.setStatus(BookingStatus.BOOK);

        mockMvc.perform(patch("/api/course/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void booking_courseNotFound_shouldReturn404() throws Exception {
        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setUserId(testUser.getId());
        dto.setCourseId(9999L);
        dto.setStatus(BookingStatus.BOOK);

        mockMvc.perform(patch("/api/course/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void booking_courseFull_shouldReturn409() throws Exception {
        testCourse.setMaxParticipants(0);
        courseRepository.save(testCourse);

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setUserId(testUser.getId());
        dto.setCourseId(testCourse.getId());
        dto.setStatus(BookingStatus.BOOK);

        mockMvc.perform(patch("/api/course/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }
}