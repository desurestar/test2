package ru.zagrebin.laba11;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ru.zagrebin.laba11.dto.*;
import ru.zagrebin.laba11.entity.Address;
import ru.zagrebin.laba11.entity.Student;
import ru.zagrebin.laba11.entity.StudentGroup;
import ru.zagrebin.laba11.form.StudentForm;
import ru.zagrebin.laba11.service.ServiceAddress;
import ru.zagrebin.laba11.service.ServiceCourse;
import ru.zagrebin.laba11.service.ServiceStudent;
import ru.zagrebin.laba11.service.ServiceStudentGroup;
import ru.zagrebin.laba11.service.ServiceUniversity;

@RequestMapping("/api/student")
@RestController
public class StudentRestController {
    @Autowired
    private final ServiceStudent studentService;

    public StudentRestController(ServiceStudent studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StudentStatistics> getStudentStatistics() {
        StudentStatistics statistics = studentService.getStudentStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/")
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id).orElseThrow(()->new EntityNotFoundException("Student not found with id " + id));
        return new ResponseEntity<>(convertToDTO(student), HttpStatus.OK);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentCreateDTO studentCreateDTO) {
        Student student = studentService.createStudentFromDTO(studentCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id,
                                                    @Valid @RequestBody StudentUpdateDTO studentUpdateDTO) {
        Student student = studentService.updateStudentFromDTO(id, studentUpdateDTO);
        return ResponseEntity.ok(convertToDTO(student));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
    }

    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setLastName(student.getLastName());
        dto.setFirstName(student.getFirstName());
        dto.setPatronymic(student.getPatronymic());
        dto.setGender(student.getGender());
        dto.setNationality(student.getNationality());
        dto.setHeight(student.getHeight());
        dto.setWeight(student.getWeight());
        dto.setBirthDate(student.getBirthDate());
        dto.setPhoneNumber(student.getPhoneNumber());
        dto.setGpa(student.getGpa());
        dto.setSpeciality(student.getSpeciality());

        if (student.getAddress() != null) {
            dto.setAddressId(student.getAddress().getId());
            dto.setCity(student.getAddress().getCity());
            dto.setStreet(student.getAddress().getStreet());
            dto.setHouse(student.getAddress().getHouse());
            dto.setApartment(student.getAddress().getApartment());
        }

        if (student.getStudentGroup() != null) {
            dto.setStudentGroupId(student.getStudentGroup().getId());
            dto.setGroupName(student.getStudentGroup().getName());
            if (student.getStudentGroup().getUniversity() != null) {
                dto.setUniversityName(student.getStudentGroup().getUniversity().getName());
            }
        }

        if (student.getCourses() != null) {
            dto.setCourseIds(student.getCourses().stream()
                    .map(course -> course.getId())
                    .collect(Collectors.toList()));
            dto.setCourseNames(student.getCourses().stream()
                    .map(course -> course.getName())
                    .collect(Collectors.toList()));
        }

        return dto;
    }

}
