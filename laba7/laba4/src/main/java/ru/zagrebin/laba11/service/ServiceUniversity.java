package ru.zagrebin.laba11.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zagrebin.laba11.entity.University;
import ru.zagrebin.laba11.repository.UniversityRepository;

import java.util.List;

@Service
public class ServiceUniversity {
    @Autowired
    private UniversityRepository universityRepository;

    public List<University> findAllUniversity() {
        return (List<University>) universityRepository.findAll();
    }

    public University findUniversityById(Long id) {
        return universityRepository.findById(id).get();
    }

    public void saveUniversity(University university) {
        universityRepository.save(university);
    }

    public void deleteUniversityById(Long id) {
        universityRepository.deleteById(id);
    }

    public List<University> findByName(String name) {
        return universityRepository.findByNameContainingIgnoreCase(name);
    }
}