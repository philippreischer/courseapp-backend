package at.codersbay.courseapp.api.course.booking;

import java.time.LocalDate;

public class BookingResponseDTO {
    private long userId;
    private String userName;
    private LocalDate bookedDate;
    private BookingStatus status;

    public BookingResponseDTO(Booking booking) {
        this.userId = booking.getUser().getId();
        this.userName = booking.getUser().getUserName();
        this.bookedDate = booking.getBookedDate();
        this.status = booking.getStatus();
    }

    public long getUserId() { return userId; }

    public String getUserName() { return userName; }

    public LocalDate getBookedDate() { return bookedDate; }

    public BookingStatus getStatus() {
        return status;
    }
}
