package at.codersbay.courseapp.api.course.update;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course")
public class UpdateCourseController {

    @Autowired
    private UpdateCourseService updateCourseService;

    @PutMapping
    public ResponseEntity<CourseResponseBody> update(
            @RequestBody UpdateCourseRequestDTO updateCourseDTO) {

            Course course = updateCourseService.update(updateCourseDTO);
            return ResponseEntity.ok(new CourseResponseBody(course));
    }
}