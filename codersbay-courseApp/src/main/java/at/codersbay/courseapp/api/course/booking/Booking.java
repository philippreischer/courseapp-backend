package at.codersbay.courseapp.api.course.booking;
import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="TB_BOOKED")
public class Booking {

    @EmbeddedId
    private BookingId id;

    @ManyToOne
    @MapsId("courseId")
    @JsonBackReference("course-booking")
    private Course course;

    @ManyToOne
    @MapsId("userId")
    @JsonBackReference("user-booking")
    private User user;

    private LocalDate bookedDate;


    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    protected Booking() {
    }

    public Booking(Course course, User user, BookingStatus status) {
        this.id = new BookingId(course.getId(), user.getId());
        this.course = course;
        this.user = user;
        this.bookedDate = LocalDate.now();
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public BookingId getId() {
        return id;
    }

    public void setId(BookingId id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(LocalDate bookedDate) {
        this.bookedDate = bookedDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
