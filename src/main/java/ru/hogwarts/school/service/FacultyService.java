package ru.hogwarts.school.service;
import org.apache.commons.logging.Log;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FacultyService {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;


    public FacultyService(FacultyRepository facultyRepository, FacultyMapper facultyMapper, StudentRepository studentRepository, StudentMapper studentMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public FacultyDtoOut create(FacultyDtoIn facultyDtoIn) {
        LOG.info("Was invoked method create with parameter: {}");
        return facultyMapper.toDto(facultyRepository.save(facultyMapper.toEntity(facultyDtoIn)));
    }

    public FacultyDtoOut update(long id, FacultyDtoIn facultyDtoIn) {
        LOG.info("Was invoked method update with id");
        return facultyRepository.findById(id)
                .map(oldFaculty -> {oldFaculty.setColor(facultyDtoIn.getColor());
                    oldFaculty.setName(facultyDtoIn.getName());
                return facultyMapper.toDto(facultyRepository.save(oldFaculty));})
                .orElseThrow(()-> new FacultyNotFoundException(id));

    }

    public FacultyDtoOut delete(long id) {
        LOG.info("Was invoked method delete with id");
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(()-> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }

    public FacultyDtoOut get(long id) {
        LOG.info("Was invoked method get with id");
        return facultyRepository.findById(id)
                .map(facultyMapper::toDto)
                .orElseThrow(()-> new FacultyNotFoundException(id));
    }

    public List<FacultyDtoOut> findAll(@Nullable String color) {
        LOG.info("Was invoked method findAll");
        return Optional.ofNullable(color)
                .map(facultyRepository::findAllByColor)
                .orElseGet(facultyRepository::findAll).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<FacultyDtoOut> findByColorOrName(String colorOrName) {
        LOG.info("Was invoked method findByColorOrName");
        return facultyRepository.findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(colorOrName, colorOrName)
                .stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findstudents(long id) {
        LOG.info("Was invoked method findstudents");
        return studentRepository.findAllByFaculty_Id(id).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public String getLongestName() {
        return facultyRepository.findAll().stream()
                .map(faculty -> faculty.getName())
                .max(Comparator.comparing(name-> name.length()))
                .get();
    }

    public Integer sum() {
        long start = System.currentTimeMillis();
        int res =  Stream.iterate(1, a-> a+1)
                .limit(1_000_000)
                .reduce(0, (a,b)-> a + b);
        long finish = System.currentTimeMillis();
        long dif = finish - start;
        LOG.info("simple"+ dif);
        return res;
    }

    public Integer sumImpr() {
        long start = System.currentTimeMillis();
        int res =  Stream.iterate(1, a-> a+1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a,b)-> a + b);
        long finish = System.currentTimeMillis();
        long dif = finish - start;
        LOG.info("impr:"+ dif);
        return res;
    }
}
