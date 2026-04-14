package at.codersbay.courseapp.api.course.delete;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.exceptions.CourseDeletionException;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteCourseService {

    @Autowired
    private CourseRepository courseRepository;

    public void deleteById(long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isEmpty()) {
            throw new CourseNotFoundException(
                    "could not find course by id '" + id + "'.");
        }

        courseRepository.deleteById(id);

        if (courseRepository.findById(id).isPresent()) {
            throw new CourseDeletionException(
                    "could not delete course by id '" + id + "'.");
        }
    }
}
