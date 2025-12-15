package ru.zagrebin.laba11.dto;

import lombok.Data;

@Data
public class StudentStatistics {
    private Long totalStudents;
    private Double averageGPA;
    private Double maxGPA;
    private Double minGPA;
    private Long studentsWithHighGPA;
    private String mostPopularSpeciality;
    private String cityWithMostStudents;

    private Long maleCount;
    private Long femaleCount;

    private String largestGroup;
    private Long studentsInLargestGroup;
}