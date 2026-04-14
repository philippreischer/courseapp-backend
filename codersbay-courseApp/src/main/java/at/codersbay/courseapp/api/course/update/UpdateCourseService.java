package at.codersbay.courseapp.api.course.update;

import at.codersbay.courseapp.api.course.Course;
import at.codersbay.courseapp.api.course.CourseRepository;
import at.codersbay.courseapp.api.course.exceptions.CourseNotFoundException;
import at.codersbay.courseapp.api.exceptions.InvalidUpdateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateCourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course update(UpdateCourseRequestDTO updateCourseDTO) {

        if (updateCourseDTO == null || updateCourseDTO.getId() == null || updateCourseDTO.getId() < 1) {
            throw new InvalidUpdateException("invalid update data");
        }

        Optional<Course> optionalCourse = courseRepository.findById(updateCourseDTO.getId());

        if (optionalCourse.isEmpty()) {
            throw new CourseNotFoundException(
                    "could not find course by id '" + updateCourseDTO.getId() + "'.");
        }

        Course course = optionalCourse.get();

        if (!StringUtils.isEmpty(updateCourseDTO.getTitle())) {
            course.setTitle(updateCourseDTO.getTitle());
        }

        if (!StringUtils.isEmpty(updateCourseDTO.getDescription())) {
            course.setDescription(updateCourseDTO.getDescription());
        }

        if (updateCourseDTO.getStartDate() != null) {
            course.setStartDate(updateCourseDTO.getStartDate());
        }

        if (updateCourseDTO.getEndDate() != null) {
            course.setEndDate(updateCourseDTO.getEndDate());
        }

        if (updateCourseDTO.getMaxParticipants() != null) {
            course.setMaxParticipants(updateCourseDTO.getMaxParticipants());
        }

        return courseRepository.save(course);
    }
}
