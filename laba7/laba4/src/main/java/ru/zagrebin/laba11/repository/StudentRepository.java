package ru.zagrebin.laba11.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.zagrebin.laba11.entity.Student;


@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findByLastName(String name);
    List<Student> findByLastNameContainingIgnoreCase(String lastName);
    List<Student> findByGpaGreaterThanEqual(Double gpa);

    @Query("select s from Student s join s.address a where LOWER(a.city) like LOWER(concat('%', :city, '%'))")
    List<Student> findByCity(@Param("city") String city);

    @Query("select s from Student s join s.studentGroup g where LOWER(g.name) like LOWER(concat('%', :groupName, '%'))")
    List<Student> findByGroupName(@Param("groupName") String groupName);

    @Query("select s from Student s join s.studentGroup g where LOWER(g.speciality) like LOWER(concat('%', :speciality, '%'))")
    List<Student> findBySpeciality(@Param("speciality") String speciality);

    @Query("select s from Student s where LOWER(s.lastName) like LOWER(concat('%', :lastName, '%')) and s.gpa >= :gpa")
    List<Student> findByLastNameAndGpa(@Param("lastName") String lastName, @Param("gpa") Double gpa);

    @Query("select s from Student s join s.address a join s.studentGroup g " +
            "where LOWER(a.city) like LOWER(concat('%', :city, '%')) " +
            "and LOWER(g.speciality) like LOWER(concat('%', :speciality, '%'))")
    List<Student> findByCityAndSpeciality(@Param("city") String city, @Param("speciality") String speciality);

    @Query("select s from Student s join s.studentGroup g " +
            "where LOWER(g.name) like LOWER(concat('%', :groupName, '%')) " +
            "and LOWER(s.lastName) like LOWER(concat('%', :lastName, '%'))")
    List<Student> findByLastNameAndGroupName(@Param("lastName") String lastName, @Param("groupName") String groupName);

    
    @Query("select s from Student s left join s.address a " +
            "where (:lastName is null or LOWER(s.lastName) like LOWER(concat('%', :lastName, '%'))) " +
            "and (:city is null or LOWER(a.city) like LOWER(concat('%', :city, '%'))) " +
            "and (:minGpa is null or s.gpa >= :minGpa)")
    List<Student> searchCombined(
            @Param("lastName") String lastName,
            @Param("city") String city,
            @Param("minGpa") Double minGpa
    );
}
