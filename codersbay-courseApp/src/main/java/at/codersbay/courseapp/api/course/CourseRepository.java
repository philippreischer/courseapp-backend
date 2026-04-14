package at.codersbay.courseapp.api.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    public Optional<Course> findByTitle(String title);
}
