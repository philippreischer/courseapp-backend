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
@RequestMapping("/api/course")
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

            Course course = createCourseService.createCourse(
                    createCourseDTO.getTitle(),
                    createCourseDTO.getDescription(),
                    createCourseDTO.getStartDate(),
                    createCourseDTO.getEndDate(),
                    createCourseDTO.getMaxParticipants()
            );
            return new ResponseEntity<>(new CourseResponseBody(course), HttpStatus.CREATED);

    }
}
