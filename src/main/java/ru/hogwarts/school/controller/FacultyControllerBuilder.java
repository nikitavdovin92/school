package ru.hogwarts.school.controller;

import ru.hogwarts.school.service.FacultyService;

public class FacultyControllerBuilder {
    private FacultyService facultyService;

    public FacultyControllerBuilder setFacultyService(FacultyService facultyService) {
        this.facultyService = facultyService;
        return this;
    }

    public FacultyController createFacultyController() {
        return new FacultyController(facultyService);
    }
}