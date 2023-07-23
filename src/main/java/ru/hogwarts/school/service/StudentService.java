package ru.hogwarts.school.service;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entities.Avatar;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StudentService {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(StudentService.class);

        private final StudentRepository studentRepository;
        private final StudentMapper studentMapper;

        private final FacultyRepository facultyRepository;

        private final FacultyMapper facultyMapper;

        private final AvatarService avatarService;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, FacultyRepository facultyRepository, FacultyMapper facultyMapper, AvatarService avatarService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
        this.avatarService = avatarService;
    }
    public StudentDtoOut create(StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method create with parameter: {}");
        return studentMapper.toDto(studentRepository.save(studentMapper.toEntity(studentDtoIn)));
    }

    public StudentDtoOut update(long id, StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method update with id");
        return studentRepository.findById(id)
                .map(oldStudent -> {oldStudent.setAge(studentDtoIn.getAge());
                    oldStudent.setName(studentDtoIn.getName());
                    Optional.ofNullable(studentDtoIn.getFacultyId())
                            .ifPresent(facultyId->oldStudent.setFaculty(facultyRepository.findById(facultyId)
                                    .orElseThrow(()-> new FacultyNotFoundException(facultyId))));
                    return studentMapper.toDto(studentRepository.save(oldStudent));})
                .orElseThrow(()-> new FacultyNotFoundException(id));

    }

    public StudentDtoOut delete(long id) {
        LOG.info("Was invoked method delete with id");
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }

    public StudentDtoOut get(long id) {
        LOG.info("Was invoked method get with id");
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(()-> new StudentNotFoundException(id));
    }

    public List<StudentDtoOut> findAll(@Nullable Integer age) {
        LOG.info("Was invoked method findAll");
        return Optional.ofNullable(age)
                .map(studentRepository::findAllByAge)
                .orElseGet(studentRepository::findAll).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findByAgeBetween(int ageFrom, int ageTo) {
        LOG.info("Was invoked method findByAgeBetween");
        return studentRepository.findByAgeBetween(ageFrom, ageTo).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public FacultyDtoOut findFaculty(long id) {
        LOG.info("Was invoked method findFaculty");
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(()-> new StudentNotFoundException(id));
    }

    public StudentDtoOut uploadAvatar(long id, MultipartFile multipartFile) {
        LOG.info("Was invoked method uploadAvatar");
        Student student = studentRepository.findById(id)
                        .orElseThrow(()-> new StudentNotFoundException(id));
        avatarService.create(student,multipartFile);
        return studentMapper.toDto(student);

    }

    public int getCountOfStudents() {
        LOG.info("Was invoked method getCountOfStudents");
        return studentRepository.getCountOfStudents();
    }

    public double getAverageAge() {
        LOG.info("Was invoked method getAverageAge");
        return studentRepository.getAverageAge();
    }

    @Transactional(readOnly = true)
    public List<StudentDtoOut> getLastStudents(int count) {
        LOG.info("Was invoked method getLastStudents");
        return studentRepository.getLastStudents((java.awt.print.Pageable) Pageable.ofSize(count)).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<String> getNameStartWithA() {
        return studentRepository.findAll().stream()
                .map(student -> student.getName().toUpperCase())
                .filter(name-> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAvgAge() {
        return studentRepository.findAll().stream()
                .mapToDouble(student -> student.getAge())
                .average()
                .getAsDouble();
    }


}
