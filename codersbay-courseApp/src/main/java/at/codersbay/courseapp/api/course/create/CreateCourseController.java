package at.codersbay.courseapp.api.course.create;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseResponseBody;
import at.codersbay.courseapp.api.course.exceptions.CourseAlreadyExistsException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course/")
public class CreateCourseController {

    @Autowired
    private CreateCourseService createCourseService;

    @PostMapping
    public ResponseEntity<CourseResponseBody> create(
            @RequestBody CreateCourseRequestDTO createCourseDTO){

        if (createCourseDTO == null || StringUtils.isEmpty(createCourseDTO.getTitle())
                || StringUtils.isEmpty(createCourseDTO.getDescription())
                || createCourseDTO.getStartDate() == null
                || createCourseDTO.getEndDate() == null
                || createCourseDTO.getMaxParticipants() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        CourseResponseBody body = new CourseResponseBody();

        try {
            Course course = createCourseService.createCourse(
                    createCourseDTO.getTitle(),
                    createCourseDTO.getDescription(),
                    createCourseDTO.getStartDate(),
                    createCourseDTO.getEndDate(),
                    createCourseDTO.getMaxParticipants()
            );
            return ResponseEntity.ok(new CourseResponseBody(course));
        } catch (CourseAlreadyExistsException e) {
            body.addErrorMessage(e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);

        } catch(Throwable t) {
            body.addErrorMessage("Es ist ein Fehler aufgetreten");
            body.addErrorMessage(t.getMessage());

            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
