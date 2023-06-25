package ru.hogwarts.school.controller;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entities.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable("id") long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @GetMapping
    public Student get(@PathVariable("id") long id) {
        return studentService.get(id);
    }

    @DeleteMapping
    public Student delete(@PathVariable("id") long id) {
        return studentService.delete(id); }

    @GetMapping
    public List<Student> findAll(@RequestParam(required = false) Integer age) {
        return studentService.findAll(age);
    }
}
