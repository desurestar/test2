package ru.zagrebin.laba11.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zagrebin.laba11.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    List<Course> findByNameContainingIgnoreCase(String name);
    List<Course> findBySemesterContainingIgnoreCase(String semester);

    List<Course> findByHoursGreaterThanEqual(@Param("minHours") String minHours);
}
