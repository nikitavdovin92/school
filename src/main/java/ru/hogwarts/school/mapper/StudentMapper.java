package ru.hogwarts.school.mapper;


import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entities.Avatar;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Optional;

@Component
public class StudentMapper {
    private final FacultyMapper facultyMapper;
    private final FacultyRepository facultyRepository;
    private final AvatarMapper avatarMapper;

    public StudentMapper(FacultyMapper facultyMapper, FacultyRepository facultyRepository, AvatarMapper avatarMapper) {
        this.facultyMapper = facultyMapper;
        this.facultyRepository = facultyRepository;
        this.avatarMapper = avatarMapper;
    }

    public StudentDtoOut toDto(Student student) {
        StudentDtoOut studentDtoOut = new StudentDtoOut();
        studentDtoOut.setId(student.getId());
        studentDtoOut.setName(student.getName());
        studentDtoOut.setAge(student.getAge());
        Optional.ofNullable(student.getFaculty())
                .ifPresent(faculty -> studentDtoOut.setFaculty(facultyMapper.toDto(faculty)));
        Optional.ofNullable(student.getAvatar())
                .ifPresent(avatar -> studentDtoOut.setAvatar(avatarMapper.toDto(avatar)));
        return studentDtoOut;
    }

    public Student toEntity(StudentDtoIn studentDtoIn) {
        Student student = new Student();
        student.setAge(studentDtoIn.getAge());
        student.setName(studentDtoIn.getName());
        Optional.ofNullable(studentDtoIn.getFacultyId())
                .ifPresent(facultyId->facultyRepository
                        .findById(facultyId).orElseThrow(()-> new FacultyNotFoundException(facultyId)));
        return student;
    }
}
