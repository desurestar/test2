package ru.zagrebin.laba11.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.zagrebin.laba11.dto.StudentCreateDto;
import ru.zagrebin.laba11.dto.StudentDto;
import ru.zagrebin.laba11.dto.StudentUpdateDto;
import ru.zagrebin.laba11.entity.Student;
import ru.zagrebin.laba11.repository.StudentRepository;

@Service
public class ServiceStudent {
    @Autowired
    private StudentRepository studentRepository;

    public List<StudentDto> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    public Optional<StudentDto> getStudentById(Long id) {
        return Optional.ofNullable(studentRepository.getStudentById(id));
    }

    public StudentDto createStudent(StudentCreateDto student) {
        return studentRepository.saveStudent(student);
    }

    public StudentDto updateStudent(Long id, StudentUpdateDto student) {
        return studentRepository.updateStudent(student, id);
    }

    public void deleteStudentById(Long id) {

    }

    public boolean existsById(Long id) {
        return false;
    }

    public List<Student> findByLastName(String lastName) {
        return null;
    }

    public List<Student> findByGpaGreaterThanEqual(Double gpa) {
        return null;
    }

    public List<Student> findByCity(String city) {
        return null;
    }

    public List<Student> findByGroupName(String groupName) {
        return null;
    }

    public List<Student> findBySpeciality(String speciality) {
        return null;
    }

    public List<Student> findByLastNameAndGpa(String lastName, Double gpa) {
        return null;
    }

    public List<Student> findByCityAndSpeciality(String city, String speciality) {
        return null;
    }

    public List<Student> findByFirstNameAndGroupName(String firstName, String groupName) {
        return null;
    }

    public List<Student> searchCombined(String lastName, String city, Double minGpa) {
        return null;
    }

    private String normalize(String value) {
        return null;
    }
}