package ru.zagrebin.laba11.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentGroupDto {
    private Long id;

    private String name;
    private String speciality;

    private UniversityDto university;

    private List<StudentDto> students;
}
