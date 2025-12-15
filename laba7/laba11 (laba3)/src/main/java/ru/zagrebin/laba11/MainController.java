package ru.zagrebin.laba11;

import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.zagrebin.laba11.dto.*;
import ru.zagrebin.laba11.entity.Student;
import ru.zagrebin.laba11.form.StudentForm;
import ru.zagrebin.laba11.service.ServiceAddress;
import ru.zagrebin.laba11.service.ServiceCourse;
import ru.zagrebin.laba11.service.ServiceStudent;
import ru.zagrebin.laba11.service.ServiceStudentGroup;
import ru.zagrebin.laba11.service.ServiceUniversity;

@Controller
public class MainController {
    private final ServiceStudent serviceStudent;
    private final ServiceAddress serviceAddress;
    private final ServiceStudentGroup serviceStudentGroup;
    @SuppressWarnings("unused")
    private final ServiceUniversity serviceUniversity;
    @SuppressWarnings("unused")
    private final ServiceCourse serviceCourse;

    @Autowired
    public MainController(ServiceStudent serviceStudent, ServiceAddress serviceAddress, ServiceStudentGroup serviceStudentGroup, ServiceUniversity serviceUniversity, ServiceCourse serviceCourse) {
        this.serviceStudent = serviceStudent;
        this.serviceAddress = serviceAddress;
        this.serviceStudentGroup = serviceStudentGroup;
        this.serviceUniversity = serviceUniversity;
        this.serviceCourse = serviceCourse;
    }

    @GetMapping("/institut")
    public String institut(Model model) {
        model.addAttribute("students", serviceStudent.getAllStudents());
        return "main";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        Optional<StudentDto> selectedStudent = serviceStudent.getStudentById(id);
        if (selectedStudent.isPresent()) {
            model.addAttribute("student", selectedStudent.get());
            return "details";
        }
        return "redirect:/institut";
    }

    @GetMapping("/students/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        serviceStudent.deleteStudentById(id);
        return "redirect:/institut";
    }

    @GetMapping("/students/addStudentForm")
    public String addStudentForm(Model model) {
        model.addAttribute("studentForm", new StudentForm());
        model.addAttribute("groups", serviceStudentGroup.findAllStudentGroup());
        return "addStudentForm";
    }

    @PostMapping("/students/addStudentForm")
    public String addStudent(@Valid @ModelAttribute StudentForm studentForm, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("groups", serviceStudentGroup.findAllStudentGroup());
            return "addStudentForm";
        }

        StudentCreateDto student = convertFormToCreateRequest(studentForm);

        try {
            StudentDto createdStudent = serviceStudent.createStudent(student);
            return "redirect:/institut";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании студента: " + e.getMessage());
            model.addAttribute("groups", serviceStudentGroup.findAllStudentGroup());
            return "addStudentForm";
        }
    }


    @GetMapping("/students/editStudent/{id}")
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        Optional<StudentDto> student = serviceStudent.getStudentById(id);
        if (student.isPresent()) {
            StudentForm studentForm = new StudentForm();
            convertEntityToForm(student.get(), studentForm);

            model.addAttribute("studentForm", studentForm);
            model.addAttribute("groups", serviceStudentGroup.findAllStudentGroup());
            return "addStudentForm";
        }
        return "redirect:/institut";
    }

    @PostMapping("/student/editStudent/{id}")
    public String editStudent(@PathVariable(name = "id") Long id, @Valid @ModelAttribute StudentForm studentForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("groups", serviceStudentGroup.findAllStudentGroup());
            return "addStudentForm";
        }

        Optional<StudentDto> existingStudent = serviceStudent.getStudentById(id);
        if (existingStudent.isPresent()) {
            StudentCreateDto student = new StudentCreateDto();

            serviceStudent.createStudent(student);
        }

        return "redirect:/institut";
    }

    @GetMapping("/students/search")
    public String searchForm(Model model) {
        model.addAttribute("searchType", "lastName");
        model.addAttribute("ln", "");
        model.addAttribute("ct", "");
        model.addAttribute("mg", "");
        return "search";
    }

    @PostMapping("/students/search")
    public String searchResults(@RequestParam String searchType,
                                @RequestParam String searchValue,
                                Model model) {
        switch (searchType) {
            case "lastName":
                model.addAttribute("students", serviceStudent.findByLastName(searchValue));
                break;
            case "city":
                model.addAttribute("students", serviceStudent.findByCity(searchValue));
                break;
            case "gpa":
                try {
                    Double gpa = Double.parseDouble(searchValue);
                    model.addAttribute("students", serviceStudent.findByGpaGreaterThanEqual(gpa));
                } catch (NumberFormatException e) {
                    model.addAttribute("error", "Введите корректное число для GPA");
                }
                break;
            case "group":
                model.addAttribute("students", serviceStudent.findByGroupName(searchValue));
                break;
            case "speciality":
                model.addAttribute("students", serviceStudent.findBySpeciality(searchValue));
                break;
        }

        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        return "search";
    }

    @PostMapping("/students/search/combined")
    public String searchCombined(@RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String city,
                                 @RequestParam(required = false) String minGpa,
                                 Model model) {
        Double parsedGpa = null;
        if (minGpa != null && !minGpa.trim().isEmpty()) {
            try {
                parsedGpa = Double.parseDouble(minGpa.trim());
            } catch (NumberFormatException e) {
                model.addAttribute("error", "Введите корректное число для минимального GPA");
            }
        }

        model.addAttribute("students", serviceStudent.searchCombined(lastName, city, parsedGpa));
        model.addAttribute("ln", lastName);
        model.addAttribute("ct", city);
        model.addAttribute("mg", minGpa);
        model.addAttribute("searchType", "combined");
        return "search";
    }

//    private void convertFormToEntity(StudentForm form, StudentDto student) {
//        student.setId(form.getId());
//        student.setLastName(form.getLastName());
//        student.setFirstName(form.getFirstName());
//        student.setPatronymic(form.getPatronymic());
//        student.setGender(form.getGender());
//        student.setNationality(form.getNationality());
//        student.setHeight(form.getHeight());
//        student.setWeight(form.getWeight());
//        student.setBirthDate(form.getBirthDate());
//        student.setPhoneNumber(form.getPhoneNumber());
//        student.setGpa(form.getGpa());
//
//        boolean hasAddress = (form.getCity() != null && !form.getCity().trim().isEmpty())
//                || (form.getStreet() != null && !form.getStreet().trim().isEmpty())
//                || (form.getHouse() != null && !form.getHouse().trim().isEmpty())
//                || (form.getApartment() != null && !form.getApartment().trim().isEmpty());
//
//        if (hasAddress) {
//            AddressDto addressToUse = null;
//            if (form.getId() != null) {
//                Optional<StudentDto> existing = serviceStudent.getStudentById(form.getId());
//                if (existing.isPresent()) {
//                    addressToUse = existing.get().getAddress();
//                }
//            }
//            if (addressToUse == null) {
//                addressToUse = new AddressDto();
//            }
//            addressToUse.setCity(form.getCity());
//            addressToUse.setStreet(form.getStreet());
//            addressToUse.setHouse(form.getHouse());
//            addressToUse.setApartment(form.getApartment());
//
//            serviceAddress.SaveAddress(addressToUse);
//            student.setAddress(addressToUse);
//        } else {
//            student.setAddress(null);
//        }
//
//        if (form.getStudentGroupId() != null) {
//            StudentGroupDto group = serviceStudentGroup.findStudentGroupById(form.getStudentGroupId());
//            student.setStudentGroup(group);
//        }
//    }

    private void convertEntityToForm(StudentDto student, StudentForm form) {
        form.setId(student.getId());
        form.setLastName(student.getLastName());
        form.setFirstName(student.getFirstName());
        form.setPatronymic(student.getPatronymic());
        form.setGender(student.getGender());
        form.setNationality(student.getNationality());
        form.setHeight(student.getHeight());
        form.setWeight(student.getWeight());
        form.setBirthDate(student.getBirthDate());
        form.setPhoneNumber(student.getPhoneNumber());
        form.setGpa(student.getGpa());

        if (student.getAddress() != null) {
            form.setCity(student.getAddress().getCity());
            form.setStreet(student.getAddress().getStreet());
            form.setHouse(student.getAddress().getHouse());
            form.setApartment(student.getAddress().getApartment());
        }

        if (student.getStudentGroup() != null) {
            form.setStudentGroupId(student.getStudentGroup().getId());
        }
    }

    private StudentCreateDto convertFormToCreateRequest(StudentForm form) {
        StudentCreateDto request = new StudentCreateDto();

        request.setLastName(form.getLastName());
        request.setFirstName(form.getFirstName());
        request.setPatronymic(form.getPatronymic());
        request.setGender(form.getGender());
        request.setNationality(form.getNationality());
        request.setHeight(form.getHeight());
        request.setWeight(form.getWeight());
        request.setBirthDate(form.getBirthDate());
        request.setPhoneNumber(form.getPhoneNumber());
        request.setGpa(form.getGpa());
        request.setSpeciality("123");


        request.setCity(form.getCity());
        request.setStreet(form.getStreet());
        request.setHouse(form.getHouse());
        request.setApartment(form.getApartment());

        request.setStudentGroupId(form.getStudentGroupId());


        return request;
    }

    private StudentUpdateDto convertFormToUpdateRequest(StudentForm form) {
        StudentUpdateDto request = new StudentUpdateDto();

        if (form.getLastName() != null) request.setLastName(form.getLastName());
        if (form.getFirstName() != null) request.setFirstName(form.getFirstName());
        if (form.getPatronymic() != null) request.setPatronymic(form.getPatronymic());
        if (form.getGender() != null) request.setGender(form.getGender());
        if (form.getNationality() != null) request.setNationality(form.getNationality());
        if (form.getHeight() != null) request.setHeight(form.getHeight());
        if (form.getWeight() != null) request.setWeight(form.getWeight());
        if (form.getBirthDate() != null) request.setBirthDate(form.getBirthDate());
        if (form.getPhoneNumber() != null) request.setPhoneNumber(form.getPhoneNumber());
        if (form.getGpa() != null) request.setGpa(form.getGpa());
        request.setSpeciality("form.getSpeciality()");

        if (form.getCity() != null) request.setCity(form.getCity());
        if (form.getStreet() != null) request.setStreet(form.getStreet());
        if (form.getHouse() != null) request.setHouse(form.getHouse());
        if (form.getApartment() != null) request.setApartment(form.getApartment());

        request.setStudentGroupId(form.getStudentGroupId());

        return request;
    }

}
