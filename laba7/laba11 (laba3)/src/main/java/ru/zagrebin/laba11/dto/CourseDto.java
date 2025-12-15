package ru.zagrebin.laba11.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private Long id;

    private String name;
    private String semester;
    private String hours;

    private List<StudentDto> students;
}
