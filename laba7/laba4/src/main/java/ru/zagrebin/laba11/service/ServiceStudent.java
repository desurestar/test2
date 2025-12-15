package ru.zagrebin.laba11.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.zagrebin.laba11.dto.StudentCreateDTO;
import ru.zagrebin.laba11.dto.StudentDTO;
import ru.zagrebin.laba11.dto.StudentStatistics;
import ru.zagrebin.laba11.dto.StudentUpdateDTO;
import ru.zagrebin.laba11.entity.Address;
import ru.zagrebin.laba11.entity.Course;
import ru.zagrebin.laba11.entity.Student;
import ru.zagrebin.laba11.entity.StudentGroup;
import ru.zagrebin.laba11.repository.AddressRepository;
import ru.zagrebin.laba11.repository.CourseRepository;
import ru.zagrebin.laba11.repository.StudentGroupRepository;
import ru.zagrebin.laba11.repository.StudentRepository;

@Service
public class ServiceStudent {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    StudentGroupRepository studentGroupRepository;

    public List<Student> getAllStudents() {
        return (List<Student>) studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }

    // Новые методы поиска
    public List<Student> findByLastName(String lastName) {
        return studentRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    public List<Student> findByGpaGreaterThanEqual(Double gpa) {
        return studentRepository.findByGpaGreaterThanEqual(gpa);
    }

    public List<Student> findByCity(String city) {
        return studentRepository.findByCity(city);
    }

    public List<Student> findByGroupName(String groupName) {
        return studentRepository.findByGroupName(groupName);
    }

    public List<Student> findBySpeciality(String speciality) {
        return studentRepository.findBySpeciality(speciality);
    }

    public List<Student> findByLastNameAndGpa(String lastName, Double gpa) {
        return studentRepository.findByLastNameAndGpa(lastName, gpa);
    }

    public List<Student> findByCityAndSpeciality(String city, String speciality) {
        return studentRepository.findByCityAndSpeciality(city, speciality);
    }

    public List<Student> findByFirstNameAndGroupName(String firstName, String groupName) {
        return studentRepository.findByLastNameAndGroupName(firstName, groupName);
    }

    public List<Student> searchCombined(String lastName, String city, Double minGpa) {
        String ln = normalize(lastName);
        String ct = normalize(city);
        return studentRepository.searchCombined(ln, ct, minGpa);
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public Student createStudentFromDTO(StudentCreateDTO dto) {
        Student student = new Student();
        createStudentFromDTO(student, dto);
        return studentRepository.save(student);
    }

    public Student updateStudentFromDTO(Long id, StudentUpdateDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));
        updateStudentFromDTO(student, dto);
        return studentRepository.save(student);
    }

    private void createStudentFromDTO(Student student, StudentCreateDTO dto) {

        student.setLastName(dto.getLastName());
        student.setFirstName(dto.getFirstName());
        student.setPatronymic(dto.getPatronymic());
        student.setGender(dto.getGender());
        student.setNationality(dto.getNationality());
        student.setHeight(dto.getHeight());
        student.setWeight(dto.getWeight());
        student.setBirthDate(dto.getBirthDate());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setGpa(dto.getGpa());
        student.setSpeciality(dto.getSpeciality());

        Address address = new Address();
        address.setCity(dto.getCity());
        address.setStreet(dto.getStreet());
        address.setHouse(dto.getHouse());
        address.setApartment(dto.getApartment());
        address = addressRepository.save(address);
        student.setAddress(address);

        StudentGroup group = studentGroupRepository.findById(dto.getStudentGroupId())
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
        student.setStudentGroup(group);

        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            List<Course> courses = (List<Course>) courseRepository.findAllById(dto.getCourseIds());
            student.setCourses(courses);
        }
    }

    private void updateStudentFromDTO(Student student, StudentUpdateDTO dto) {
        if (dto.getLastName() != null) {
            student.setLastName(dto.getLastName());
        }
        if (dto.getFirstName() != null) {
            student.setFirstName(dto.getFirstName());
        }
        if (dto.getPatronymic() != null) {
            student.setPatronymic(dto.getPatronymic());
        }
        if (dto.getGender() != null) {
            student.setGender(dto.getGender());
        }
        if (dto.getNationality() != null) {
            student.setNationality(dto.getNationality());
        }
        if (dto.getHeight() != null) {
            student.setHeight(dto.getHeight());
        }
        if (dto.getWeight() != null) {
            student.setWeight(dto.getWeight());
        }
        if (dto.getBirthDate() != null) {
            student.setBirthDate(dto.getBirthDate());
        }
        if (dto.getPhoneNumber() != null) {
            student.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getGpa() != null) {
            student.setGpa(dto.getGpa());
        }
        if (dto.getSpeciality() != null) {
            student.setSpeciality(dto.getSpeciality());
        }

        if (dto.getCity() != null || dto.getStreet() != null ||
                dto.getHouse() != null || dto.getApartment() != null) {

            Address address = (student.getAddress() != null) ? student.getAddress() : new Address();
            if (dto.getCity() != null) address.setCity(dto.getCity());
            if (dto.getStreet() != null) address.setStreet(dto.getStreet());
            if (dto.getHouse() != null) address.setHouse(dto.getHouse());
            if (dto.getApartment() != null) address.setApartment(dto.getApartment());

            address = addressRepository.save(address);
            student.setAddress(address);
        }

        if (dto.getStudentGroupId() != null) {
            StudentGroup group = studentGroupRepository.findById(dto.getStudentGroupId())
                    .orElseThrow(() -> new RuntimeException("Группа не найдена"));
            student.setStudentGroup(group);
        }

        if (dto.getCourseIds() != null) {
            List<Course> courses = (List<Course>) courseRepository.findAllById(dto.getCourseIds());
            student.setCourses(courses);
        }
    }

    public StudentStatistics getStudentStatistics() {
        StudentStatistics stats = new StudentStatistics();
        List<Student> allStudents = (List<Student>) studentRepository.findAll();

        if (allStudents.isEmpty()) {
            return initializeEmptyStatistics();
        }

        stats.setTotalStudents((long) allStudents.size());
        stats.setAverageGPA(allStudents.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0));
        stats.setMaxGPA(allStudents.stream()
                .mapToDouble(Student::getGpa)
                .max()
                .orElse(0.0));
        stats.setMinGPA(allStudents.stream()
                .mapToDouble(Student::getGpa)
                .min()
                .orElse(0.0));
        stats.setStudentsWithHighGPA(allStudents.stream()
                .filter(s -> s.getGpa() >= 4.0)
                .count());

        stats.setMostPopularSpeciality(findMostPopularSpeciality(allStudents));


        stats.setCityWithMostStudents(findCityWithMostStudents(allStudents));

        stats.setMaleCount(countStudentsByGender(allStudents, "Мужской"));
        stats.setFemaleCount(countStudentsByGender(allStudents, "Женский"));

        findLargestGroupInfo(allStudents, stats);

        return stats;
    }

    private StudentStatistics initializeEmptyStatistics() {
        StudentStatistics stats = new StudentStatistics();
        stats.setTotalStudents(0L);
        stats.setAverageGPA(0.0);
        stats.setMaxGPA(0.0);
        stats.setMinGPA(0.0);
        stats.setStudentsWithHighGPA(0L);
        stats.setMaleCount(0L);
        stats.setFemaleCount(0L);
        return stats;
    }

    private String findMostPopularSpeciality(List<Student> students) {
        return students.stream()
                .filter(s -> s.getSpeciality() != null && !s.getSpeciality().trim().isEmpty())
                .collect(Collectors.groupingBy(Student::getSpeciality, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Нет данных");
    }

    private String findCityWithMostStudents(List<Student> students) {
        return students.stream()
                .filter(s -> s.getAddress() != null && s.getAddress().getCity() != null)
                .collect(Collectors.groupingBy(s -> s.getAddress().getCity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Нет данных");
    }

    private Long countStudentsWithoutAddress(List<Student> students) {
        return students.stream()
                .filter(s -> s.getAddress() == null)
                .count();
    }

    private Long countStudentsByGender(List<Student> students, String gender) {
        return students.stream()
                .filter(s -> gender.equals(s.getGender()))
                .count();
    }

    private void findLargestGroupInfo(List<Student> students, StudentStatistics stats) {
        Map<String, Long> groupCounts = students.stream()
                .filter(s -> s.getStudentGroup() != null && s.getStudentGroup().getName() != null)
                .collect(Collectors.groupingBy(s -> s.getStudentGroup().getName(), Collectors.counting()));

        if (!groupCounts.isEmpty()) {
            Map.Entry<String, Long> largestGroup = groupCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get();

            stats.setLargestGroup(largestGroup.getKey());
            stats.setStudentsInLargestGroup(largestGroup.getValue());
        } else {
            stats.setLargestGroup("Нет данных");
            stats.setStudentsInLargestGroup(0L);
        }
    }


}