package ru.zagrebin.laba11.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.zagrebin.laba11.dto.StudentCreateDto;
import ru.zagrebin.laba11.dto.StudentDto;
import ru.zagrebin.laba11.dto.StudentUpdateDto;
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
        studentRepository.deleteStudent(id);
    }

    public boolean existsById(Long id) {
        try {
            return getStudentById(id).isPresent();
        } catch (RuntimeException e) {
            return false;
        }
    }

    public List<StudentDto> findByLastName(String lastName) {
        String ln = normalize(lastName);
        if (ln == null) return List.of();

        return studentRepository.getAllStudents().stream()
                .filter(s -> s.getLastName() != null && s.getLastName().toLowerCase().contains(ln.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<StudentDto> findByGpaGreaterThanEqual(Double gpa) {
        if (gpa == null) return List.of();

        return studentRepository.getAllStudents().stream()
                .filter(s -> s.getGpa() != null && s.getGpa() >= gpa)
                .collect(Collectors.toList());
    }

    public List<StudentDto> findByCity(String city) {
        String ct = normalize(city);
        if (ct == null) return List.of();

        return studentRepository.getAllStudents().stream()
                .filter(s -> s.getAddress() != null
                        && s.getAddress().getCity() != null
                        && s.getAddress().getCity().toLowerCase().contains(ct.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<StudentDto> findByGroupName(String groupName) {
        String gn = normalize(groupName);
        if (gn == null) return List.of();

        return studentRepository.getAllStudents().stream()
                .filter(s -> s.getStudentGroup() != null
                        && s.getStudentGroup().getName() != null
                        && s.getStudentGroup().getName().toLowerCase().contains(gn.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<StudentDto> findBySpeciality(String speciality) {
        String sp = normalize(speciality);
        if (sp == null) return List.of();

        return studentRepository.getAllStudents().stream()
                .filter(s -> s.getSpeciality() != null && s.getSpeciality().toLowerCase().contains(sp.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<StudentDto> findByLastNameAndGpa(String lastName, Double gpa) {
        String ln = normalize(lastName);
        if (ln == null || gpa == null) return List.of();

        return studentRepository.getAllStudents().stream()
                .filter(s -> s.getLastName() != null && s.getLastName().toLowerCase().contains(ln.toLowerCase()))
                .filter(s -> s.getGpa() != null && s.getGpa() >= gpa)
                .collect(Collectors.toList());
    }

    public List<StudentDto> findByCityAndSpeciality(String city, String speciality) {
        String ct = normalize(city);
        String sp = normalize(speciality);
        if (ct == null || sp == null) return List.of();

        return studentRepository.getAllStudents().stream()
                .filter(s -> s.getAddress() != null
                        && s.getAddress().getCity() != null
                        && s.getAddress().getCity().toLowerCase().contains(ct.toLowerCase()))
                .filter(s -> s.getSpeciality() != null && s.getSpeciality().toLowerCase().contains(sp.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<StudentDto> findByFirstNameAndGroupName(String firstName, String groupName) {
        String fn = normalize(firstName);
        String gn = normalize(groupName);
        if (fn == null || gn == null) return List.of();

        return studentRepository.getAllStudents().stream()
                .filter(s -> s.getFirstName() != null && s.getFirstName().toLowerCase().contains(fn.toLowerCase()))
                .filter(s -> s.getStudentGroup() != null
                        && s.getStudentGroup().getName() != null
                        && s.getStudentGroup().getName().toLowerCase().contains(gn.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<StudentDto> searchCombined(String lastName, String city, Double minGpa) {
        String ln = normalize(lastName);
        String ct = normalize(city);

        return studentRepository.getAllStudents().stream()
                .filter(s -> ln == null || (s.getLastName() != null && s.getLastName().toLowerCase().contains(ln.toLowerCase())))
                .filter(s -> ct == null || (s.getAddress() != null && s.getAddress().getCity() != null
                        && s.getAddress().getCity().toLowerCase().contains(ct.toLowerCase())))
                .filter(s -> minGpa == null || (s.getGpa() != null && s.getGpa() >= minGpa))
                .collect(Collectors.toList());
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
