package at.codersbay.courseapp.api.course.update;
import at.codersbay.courseapp.api.course.create.CreateCourseRequestDTO;

public class UpdateCourseRequestDTO extends CreateCourseRequestDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
