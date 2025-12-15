package ru.zagrebin.laba11.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zagrebin.laba11.entity.Course;
import ru.zagrebin.laba11.repository.CourseRepository;

import java.util.List;

@Service
public class ServiceCourse {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAllCourses() {
        return (List<Course>) courseRepository.findAll();
    }
    public Course findCourseById(Long id) {
        return courseRepository.findById(id).get();
    }

    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> findByName(String name) {
        return courseRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Course> findBySemester(String semester) {
        return courseRepository.findBySemesterContainingIgnoreCase(semester);
    }

    public List<Course> findByHoursGreaterThanEqual(String minHours) {
        return courseRepository.findByHoursGreaterThanEqual(minHours);
    }
}