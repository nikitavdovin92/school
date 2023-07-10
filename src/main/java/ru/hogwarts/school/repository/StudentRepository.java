package ru.hogwarts.school.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entities.Student;

import java.awt.print.Pageable;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge (int age);
    List<Student> findByAgeBetween (int ageFrom, int ageTo);

    List<Student> findAllByFaculty_Id(long facultyId);


    @Query("SELECT count(s) from Student s")
    int getCountOfStudents();

    @Query("SELECT avg (s.age) from Student s")
    double getAverageAge();

    @Query("SELECT new ru.hogwarts.school.dto.StudentDtoOut(s.id, s.name, s.age, f.id, f.name, f.color, a.id) from Student s left join Faculty f on s.faculty = f left join  Avatar a on a.student = s order by s.id desc ")
    List<StudentDtoOut> getLastStudents(Pageable pageable);

}
