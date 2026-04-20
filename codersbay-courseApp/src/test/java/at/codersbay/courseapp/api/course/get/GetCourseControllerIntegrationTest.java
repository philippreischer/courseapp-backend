package at.codersbay.courseapp.api.course.get;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.booking.Booking;
import at.codersbay.courseapp.api.course.booking.BookingStatus;

import at.codersbay.courseapp.api.user.User;
import at.codersbay.courseapp.api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    private UserRepository userRepository;

    private Course savedCourse;

    private LocalDate today = LocalDate.now();
    private LocalDate startDate = today.plusDays(10);
    private LocalDate endDate = today.plusDays(40);

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        courseRepository.flush();

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

    @Test
    void getAllBooked_courseWithBooking_returns200() throws Exception {
        userRepository.deleteAll();
        userRepository.flush();
        User user = userRepository.save(
                new User("maxmuster", "Max", "Mustermann",
                        "max@mustermann.com", "pass123"));

        Booking booking = new Booking(savedCourse, user, BookingStatus.BOOK);
        savedCourse.getBookings().add(booking);
        courseRepository.save(savedCourse);

        Course emptyCourse = new Course("Python", "Scripting",
                today.minusDays(5), today.plusDays(15), 10);
        emptyCourse.setBookings(new HashSet<>());
        courseRepository.save(emptyCourse);

        mockMvc.perform(get("/api/course/booked"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Testing Course"))
                .andExpect(jsonPath("$[0].bookings.length()").value(1))
                .andExpect(jsonPath("$[0].bookings[0].userName").value("maxmuster"));
    }

    @Test
    void getAllBooked_noBookings_returnsEmptyList200() throws Exception {
        mockMvc.perform(get("/api/course/booked"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllCurrentDate_courseWithinRange_returns200() throws Exception {
        Course currentCourse = new Course("Aktuell", "Läuft gerade",
                today.minusDays(10), today.plusDays(10), 10);
        currentCourse.setBookings(new HashSet<>());
        courseRepository.save(currentCourse);

        mockMvc.perform(get("/api/course/currentdate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Aktuell"));
    }

    @Test
    void getAllCurrentDate_noCurrentCourses_returnsEmptyList200() throws Exception {
        mockMvc.perform(get("/api/course/currentdate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllCurrentDate_courseStartsToday_returns200() throws Exception {
        Course course = new Course("Startet heute", "Edge Case",
                today, today.plusDays(30), 10);
        course.setBookings(new HashSet<>());
        courseRepository.save(course);

        mockMvc.perform(get("/api/course/currentdate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title == 'Startet heute')]").exists());
    }

    @Test
    void getAllCurrentDate_courseEndedYesterday_notReturns200() throws Exception {
        Course pastCourse = new Course("Vergangen", "Schon vorbei",
                today.minusDays(30), today.minusDays(1), 10);
        pastCourse.setBookings(new HashSet<>());
        courseRepository.save(pastCourse);

        mockMvc.perform(get("/api/course/currentdate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)).andDo(print());
    }
}


