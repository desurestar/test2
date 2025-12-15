package ru.zagrebin.laba11.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zagrebin.laba11.entity.StudentGroup;
import ru.zagrebin.laba11.repository.StudentGroupRepository;

import java.util.List;

@Service
public class ServiceStudentGroup {
    @Autowired
    private StudentGroupRepository studentGroupRepository;

    public List<StudentGroup> findAllStudentGroup() {
        return (List<StudentGroup>) studentGroupRepository.findAll();
    }

    public StudentGroup findStudentGroupById(Long id) {
        return studentGroupRepository.findById(id).get();
    }

    public void saveStudentGroup(StudentGroup studentGroup) {
        studentGroupRepository.save(studentGroup);
    }

    public void deleteStudentGroup(Long id) {
        studentGroupRepository.deleteById(id);
    }

    public List<StudentGroup> findByName(String name) {
        return studentGroupRepository.findByNameContainingIgnoreCase(name);
    }

    public List<StudentGroup> findBySpeciality(String speciality) {
        return studentGroupRepository.findBySpecialityContainingIgnoreCase(speciality);
    }

    public List<StudentGroup> findByUniversityName(String universityName) {
        return studentGroupRepository.findByUniversityName(universityName);
    }
}