package at.codersbay.courseapp.api.course.create;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.exceptions.CourseAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CreateCourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course createCourse(String title, String description, LocalDate startDate, LocalDate endDate, int maxParticipants) {

        Optional<Course> existingTitle = courseRepository.findByTitle(title);
        if (existingTitle.isPresent()) {
            throw new CourseAlreadyExistsException("Kursname is already exist");
        }

        Course course = new Course(title, description, startDate, endDate, maxParticipants);
        return courseRepository.save(course);

    }
}
