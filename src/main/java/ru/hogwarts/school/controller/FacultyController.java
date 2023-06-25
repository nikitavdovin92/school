package ru.hogwarts.school.controller;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entities.Faculty;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;


@RestController
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @PutMapping("/{id}")
    public Faculty update(@PathVariable("id") long id, @RequestBody Faculty faculty) {
        return facultyService.update(id, faculty);
    }

    @GetMapping
    public Faculty get(@PathVariable("id") long id) {
        return facultyService.get(id);
    }

    @DeleteMapping
    public Faculty delete(@PathVariable("id") long id) {
        return facultyService.delete(id);
    }

    @GetMapping
    public List<Faculty> findAll(@RequestParam(required = false) String color) {
        return facultyService.findAll(color);
    }
}
