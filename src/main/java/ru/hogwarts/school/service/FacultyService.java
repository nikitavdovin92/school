package ru.hogwarts.school.service;


import org.springframework.lang.Nullable;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.exception.FacultyNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class FacultyService {
    private final  Map<Long, Faculty> faculties = new HashMap<>();

    private long idGenerator = 1;

    public Faculty create(Faculty faculty) {
        faculty.setId(idGenerator++);
        faculties.put(idGenerator, faculty);
        return faculty;
    }

    public Faculty update(long id, Faculty faculty) {
        if (faculties.containsKey(id)) {
            Faculty oldFaculty = faculties.get(id);
            oldFaculty.setName(faculty.getName());
            oldFaculty.setColor(faculty.getColor());
            faculties.replace(id, oldFaculty);
            return oldFaculty;
        } else {
            throw new FacultyNotFoundException(id);
        }
    }

    public Faculty delete(long id) {
        if (faculties.containsKey(id)) {
            return faculties.remove(id);
        } else {
            throw new FacultyNotFoundException(id);
        }
    }

    public Faculty get(long id) {
        if (faculties.containsKey(id)) {
            return faculties.get(id);
        } else {
            throw new FacultyNotFoundException(id);
        }
    }

    public List<Faculty> findAll(@Nullable String color) {
        return Optional.ofNullable(color)
                .map(c->faculties.values().stream().filter(faculty -> faculty.getColor().equals(c)).collect(Collectors.toList()))
                .orElseGet(()->faculties.values().stream().collect(Collectors.toList()));
    }
}
