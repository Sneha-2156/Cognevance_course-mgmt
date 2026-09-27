package com.cognevance.coursemgmt.repository;

import com.cognevance.coursemgmt.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCategoryIgnoreCase(String category);

    // Search across title and description; used by GET /api/courses?search=...
    @Query("SELECT c FROM Course c WHERE " +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> search(@Param("keyword") String keyword);
}
