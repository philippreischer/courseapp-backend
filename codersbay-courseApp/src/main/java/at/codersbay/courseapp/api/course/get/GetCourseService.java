package at.codersbay.courseapp.api.course.get;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetCourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public List<Course> getAllBooked() {
        return courseRepository.findAll().stream()
                .filter(course -> course.getBookings() != null && !course.getBookings().isEmpty())
                .collect(Collectors.toList());
    }

    public List<Course> getAllCurrentDate() {
        LocalDate today = LocalDate.now();
        return courseRepository.findAll().stream()
                .filter(course -> !today.isBefore(course.getStartDate())
                        && !today.isAfter(course.getEndDate()))
                .collect(Collectors.toList());
    }

    public Course getById(long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(
                        "could not find course by id '" + id + "'."));
    }

    public Course getByTitle(String title) {
        return courseRepository.findByTitle(title)
                .orElseThrow(() -> new CourseNotFoundException(
                        "could not find course by title '" + title + "'."));
    }
}
