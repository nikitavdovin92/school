package ru.hogwarts.school.service;
import org.springframework.lang.Nullable;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import java.util.*;
import java.util.stream.Collectors;

public class FacultyService {
    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;

    public FacultyService(FacultyRepository facultyRepository, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
    }

    public FacultyDtoOut create(FacultyDtoIn facultyDtoIn) {
        return facultyMapper.toDto(facultyRepository.save(facultyMapper.toEntity(facultyDtoIn)));
    }

    public FacultyDtoOut update(long id, FacultyDtoIn facultyDtoIn) {
        return facultyRepository.findById(id)
                .map(oldFaculty -> {oldFaculty.setColor(facultyDtoIn.getColor());
                    oldFaculty.setName(facultyDtoIn.getName());
                return facultyMapper.toDto(facultyRepository.save(oldFaculty));})
                .orElseThrow(()-> new FacultyNotFoundException(id));

    }

    public FacultyDtoOut delete(long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(()-> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }

    public FacultyDtoOut get(long id) {
        return facultyRepository.findById(id)
                .map(facultyMapper::toDto)
                .orElseThrow(()-> new FacultyNotFoundException(id));
    }

    public List<FacultyDtoOut> findAll(@Nullable String color) {
        return Optional.ofNullable(color)
                .map(facultyRepository::findAllByColor)
                .orElseGet(facultyRepository::findAll).stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }
}
