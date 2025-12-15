package ru.zagrebin.laba11.repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import ru.zagrebin.laba11.dto.AddressDto;
import ru.zagrebin.laba11.dto.CourseDto;
import ru.zagrebin.laba11.dto.StudentCreateDto;
import ru.zagrebin.laba11.dto.StudentDto;
import ru.zagrebin.laba11.dto.StudentGroupDto;
import ru.zagrebin.laba11.dto.StudentResponseDto;
import ru.zagrebin.laba11.dto.StudentUpdateDto;
import ru.zagrebin.laba11.dto.UniversityDto;


@Repository
public class StudentRepository {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final String BASE_URL = "http://localhost:8081/api/student/";


    public StudentRepository(HttpClient httpClient,  ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public List<StudentDto> getAllStudents() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                StudentResponseDto[] students = objectMapper.readValue(response.body(), StudentResponseDto[].class);
                return Arrays.stream(students)
                        .filter(Objects::nonNull)
                        .map(this::mapToStudentDto)
                        .collect(Collectors.toList());
            } else {
                throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to get students", e);
        }
    }

    public StudentDto getStudentById(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + id))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                StudentResponseDto studentDto = objectMapper.readValue(response.body(), StudentResponseDto.class);
                return mapToStudentDto(studentDto);
            } else  {
                throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to get student", e);
        }
    }

    public StudentDto saveStudent(StudentCreateDto studentDto) {
        try {

            String json = objectMapper.writeValueAsString(studentDto);


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                StudentResponseDto created = objectMapper.readValue(response.body(), StudentResponseDto.class);
                return mapToStudentDto(created);
            } else {
                throw new RuntimeException("Failed to create student, HTTP status: " + response.statusCode());

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StudentDto updateStudent(StudentUpdateDto studentUpdateDto, Long id) {


        try {
            String json = objectMapper.writeValueAsString(studentUpdateDto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if  (response.statusCode() == 200) {
                StudentResponseDto updated = objectMapper.readValue(response.body(), StudentResponseDto.class);
                return mapToStudentDto(updated);
            } else {
                throw new RuntimeException("Failed : HTTP error code : " + response.statusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStudent(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 204) {
                throw new RuntimeException("Failed to delete student: HTTP " + response.statusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete student with id: " + id, e);
        }
    }

    private StudentDto mapToStudentDto(StudentResponseDto response) {
        StudentDto dto = new StudentDto();
        dto.setId(response.getId());
        dto.setLastName(response.getLastName());
        dto.setFirstName(response.getFirstName());
        dto.setPatronymic(response.getPatronymic());
        dto.setGender(response.getGender());
        dto.setNationality(response.getNationality());
        dto.setHeight(response.getHeight());
        dto.setWeight(response.getWeight());
        dto.setBirthDate(response.getBirthDate());
        dto.setPhoneNumber(response.getPhoneNumber());
        dto.setGpa(response.getGpa());
        dto.setSpeciality(response.getSpeciality());

        if (response.getCity() != null || response.getStreet() != null || response.getHouse() != null || response.getApartment() != null) {
            AddressDto address = new AddressDto();
            address.setId(response.getAddressId());
            address.setCity(response.getCity());
            address.setStreet(response.getStreet());
            address.setHouse(response.getHouse());
            address.setApartment(response.getApartment());
            dto.setAddress(address);
        }

        if (response.getStudentGroupId() != null || response.getGroupName() != null || response.getUniversityName() != null) {
            StudentGroupDto group = new StudentGroupDto();
            group.setId(response.getStudentGroupId());
            group.setName(response.getGroupName());

            if (response.getUniversityName() != null) {
                UniversityDto university = new UniversityDto();
                university.setName(response.getUniversityName());
                group.setUniversity(university);
            }

            dto.setStudentGroup(group);
        }

        if (response.getCourseIds() != null && !response.getCourseIds().isEmpty()) {
            List<CourseDto> courses = response.getCourseIds().stream()
                    .map(id -> {
                        CourseDto course = new CourseDto();
                        course.setId(id);
                        return course;
                    })
                    .collect(Collectors.toList());

            if (response.getCourseNames() != null && !response.getCourseNames().isEmpty()) {
                for (int i = 0; i < courses.size() && i < response.getCourseNames().size(); i++) {
                    courses.get(i).setName(response.getCourseNames().get(i));
                }
            }
            dto.setCourses(courses);
        }

        return dto;
    }

}
