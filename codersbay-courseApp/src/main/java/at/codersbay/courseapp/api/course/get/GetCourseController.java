package at.codersbay.courseapp.api.course.get;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/course")
public class GetCourseController {

    @Autowired
    private GetCourseService getCourseService;

    @GetMapping
    public ResponseEntity<List<CourseResponseBody>> getAllCourses() {
        List<CourseResponseBody> courses = getCourseService.getAll().stream()
                .map(CourseResponseBody::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/booked")
    public ResponseEntity<List<CourseResponseBody>> getAllBookedCourses() {
        List<CourseResponseBody> courses = getCourseService.getAllBooked().stream()
                .map(CourseResponseBody::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/currentdate")
    public ResponseEntity<List<CourseResponseBody>> getAllCurrentDateCourses() {
        List<CourseResponseBody> courses = getCourseService.getAllCurrentDate().stream()
                .map(CourseResponseBody::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseBody> getById(@PathVariable long id) {
        try {
            Course course = getCourseService.getById(id);
            return ResponseEntity.ok(new CourseResponseBody(course));
        } catch (CourseNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/byTitle/{title}")
    public ResponseEntity<CourseResponseBody> getByTitle(@PathVariable String title) {
        try {
            Course course = getCourseService.getByTitle(title);
            return ResponseEntity.ok(new CourseResponseBody(course));
        } catch (CourseNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}