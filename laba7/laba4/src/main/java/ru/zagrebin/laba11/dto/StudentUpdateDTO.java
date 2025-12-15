package ru.zagrebin.laba11.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudentUpdateDTO {
    private String lastName;

    private String firstName;

    private String patronymic;

    private String gender;

    private String nationality;

    @Min(value = 100, message = "Рост должен быть не менее 100 см")
    private Double height;

    @Min(value = 30, message = "Вес должен быть не менее 30 кг")
    private Double weight;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    private String phoneNumber;

    @DecimalMin(value = "0.0", message = "GPA не может быть меньше 0")
    @DecimalMax(value = "5.0", message = "GPA не может быть больше 5")
    private Double gpa;

    private String speciality;

    private String city;
    private String street;
    private String house;
    private String apartment;

    private Long studentGroupId;

    private List<Long> courseIds;
}