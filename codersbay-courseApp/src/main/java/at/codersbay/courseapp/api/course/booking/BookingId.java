package at.codersbay.courseapp.api.course.booking;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BookingId implements Serializable {

    @Column(name="course_id")
    private long courseId;

    @Column(name="user_id")
    private long userId;

    public BookingId() {}

    public BookingId(long courseId, long userId) {
        this.courseId = courseId;
        this.userId = userId;
    }
}