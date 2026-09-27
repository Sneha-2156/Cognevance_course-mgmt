package com.cognevance.coursemgmt.service;

import com.cognevance.coursemgmt.entity.Course;
import com.cognevance.coursemgmt.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll(String search, String category) {
        if (search != null && !search.isBlank()) {
            return courseRepository.search(search);
        }
        if (category != null && !category.isBlank()) {
            return courseRepository.findByCategoryIgnoreCase(category);
        }
        return courseRepository.findAll();
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
    }

    public Course create(Course course) {
        return courseRepository.save(course);
    }

    public Course update(Long id, Course updated) {
        Course existing = findById(id);
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setInstructor(updated.getInstructor());
        return courseRepository.save(existing);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
