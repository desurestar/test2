package ru.zagrebin.laba11.repository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.zagrebin.laba11.dto.StudentCreateDto;
import ru.zagrebin.laba11.dto.StudentDto;
import ru.zagrebin.laba11.dto.StudentUpdateDto;
import ru.zagrebin.laba11.entity.Student;


@Repository
public class StudentRepository {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private String baseURL = "http://localhost:8081/api/student/";


    public StudentRepository(HttpClient httpClient,  ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public List<StudentDto> getAllStudents() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseURL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                StudentDto[] students = objectMapper.readValue(response.body(), StudentDto[].class);
                return Arrays.asList(students);
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
                    .uri(URI.create(baseURL + id))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                StudentDto studentDto = objectMapper.readValue(response.body(), StudentDto.class);
                return studentDto;
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
                    .uri(URI.create(baseURL + "create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return objectMapper.readValue(response.body(), StudentDto.class);
            } else {
                throw new RuntimeException();

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StudentDto updateStudent(StudentUpdateDto studentUpdateDto, Long id) {


        try {
            String json = objectMapper.writeValueAsString(studentUpdateDto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseURL + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if  (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), StudentDto.class);
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
                    .uri(URI.create(baseURL + "/" + id))
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

}
