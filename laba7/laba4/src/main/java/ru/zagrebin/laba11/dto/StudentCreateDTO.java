package ru.zagrebin.laba11.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudentCreateDTO {
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    private String patronymic;

    @NotBlank(message = "Пол обязателен")
    private String gender;

    private String nationality;

    @NotNull(message = "Рост обязателен")
    @Min(value = 100, message = "Рост должен быть не менее 100 см")
    private Double height;

    @NotNull(message = "Вес обязателен")
    @Min(value = 30, message = "Вес должен быть не менее 30 кг")
    private Double weight;

    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @NotBlank(message = "Номер телефона обязателен")
    private String phoneNumber;

    @NotNull(message = "GPA обязателен")
    @DecimalMin(value = "0.0", message = "GPA не может быть меньше 0")
    @DecimalMax(value = "5.0", message = "GPA не может быть больше 5")
    private Double gpa;

    private String speciality;


    private String city;
    private String street;
    private String house;
    private String apartment;


    @NotNull(message = "ID группы обязателен")
    private Long studentGroupId;

    private List<Long> courseIds;
}