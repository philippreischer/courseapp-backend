package at.codersbay.courseapp.api.course;

import at.codersbay.courseapp.api.course.booking.BookingResponseDTO;
import at.codersbay.courseapp.api.response.ResponseBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CourseResponseBody extends ResponseBody {

    private Course course;
    private long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxParticipants;
    private int currentParticipants;
    private List<BookingResponseDTO> bookings;

    public CourseResponseBody() {}

    public CourseResponseBody(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.startDate = course.getStartDate();
        this.endDate = course.getEndDate();
        this.maxParticipants = course.getMaxParticipants();
        this.currentParticipants = course.getBookings() != null
                ? course.getBookings().size() : 0;
        this.bookings = course.getBookings() != null
                ? course.getBookings().stream()
                .map(BookingResponseDTO::new)
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public List<BookingResponseDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingResponseDTO> bookings) {
        this.bookings = bookings;
    }
}
