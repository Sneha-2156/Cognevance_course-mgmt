package com.cognevance.coursemgmt.controller;

import com.cognevance.coursemgmt.entity.Course;
import com.cognevance.coursemgmt.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // GET /api/courses?search=java&category=Backend  (both optional, open to everyone)
    @GetMapping
    public List<Course> getCourses(@RequestParam(required = false) String search,
                                    @RequestParam(required = false) String category) {
        return courseService.findAll(search, category);
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseService.findById(id);
    }

    // Admin only (enforced in SecurityConfig)
    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(course));
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @Valid @RequestBody Course course) {
        return courseService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
