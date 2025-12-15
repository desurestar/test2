package ru.zagrebin.laba11.form;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentForm {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 50)
    private String firstName;

    private String patronymic;

    @NotBlank
    @Pattern(regexp = "Мужской|Женский")
    private String gender;

    @NotBlank
    private String nationality;

    @NotNull
    @DecimalMin(value = "100")
    @DecimalMax(value = "250")
    private Double height;

    @NotNull
    @DecimalMin(value = "25")
    @DecimalMax(value = "300")
    private Double weight;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotNull
    @Pattern(regexp = "^\\+7\\d{10}$", message="Должно соответсвовать формату +79999999999")
    private String phoneNumber;

    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private Double gpa;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String house;
    private String apartment;
    @NotNull
    private Long studentGroupId;
}
