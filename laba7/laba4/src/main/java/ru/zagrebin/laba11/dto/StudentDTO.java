package ru.zagrebin.laba11.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudentDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String gender;
    private String nationality;
    private Double height;
    private Double weight;
    private LocalDate birthDate;
    private String phoneNumber;
    private Double gpa;
    private String speciality;

    private Long addressId;
    private String city;
    private String street;
    private String house;
    private String apartment;

    private Long studentGroupId;
    private String groupName;
    private String universityName;

    private List<Long> courseIds;
    private List<String> courseNames;
}