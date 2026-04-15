package at.codersbay.courseapp.api.course.delete;

import at.codersbay.courseapp.api.course.exceptions.CourseDeletionException;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import at.codersbay.courseapp.api.response.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/course")
public class DeleteCourseController {

    @Autowired
    private DeleteCourseService deleteCourseService;

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBody> delete(@PathVariable long id) {

        ResponseBody responseBody = new ResponseBody();
        deleteCourseService.deleteById(id);
        responseBody.addMessage("Ok");

        return new ResponseEntity<>(responseBody, HttpStatus.ACCEPTED);

    }
}
