package ru.zagrebin.laba11.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StudentUpdateDto {
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

    private String city;
    private String street;
    private String house;
    private String apartment;

    private Long studentGroupId;
    private List<Long> courseIds;
}
