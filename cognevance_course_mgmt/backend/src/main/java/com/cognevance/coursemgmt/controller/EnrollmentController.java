package com.cognevance.coursemgmt.controller;

import com.cognevance.coursemgmt.dto.EnrollmentDtos.EnrollmentResponse;
import com.cognevance.coursemgmt.dto.EnrollmentDtos.ProgressUpdateRequest;
import com.cognevance.coursemgmt.entity.Enrollment;
import com.cognevance.coursemgmt.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // Student enrolls in a course. The user's identity comes from their JWT, not the request body.
    @PostMapping("/{courseId}")
    public ResponseEntity<EnrollmentResponse> enroll(@PathVariable Long courseId, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enroll(auth.getName(), courseId));
    }

    // The logged-in student's own enrollments + progress (their dashboard)
    @GetMapping("/me")
    public List<EnrollmentResponse> myEnrollments(Authentication auth) {
        return enrollmentService.findByUser(auth.getName());
    }

    @PutMapping("/{courseId}/progress")
    public EnrollmentResponse updateProgress(@PathVariable Long courseId,
                                              @RequestBody ProgressUpdateRequest req,
                                              Authentication auth) {
        return enrollmentService.updateProgress(auth.getName(), courseId, req.progress);
    }

    // Admin: see who is enrolled in a given course
    @GetMapping("/course/{courseId}")
    public List<Enrollment> byCourse(@PathVariable Long courseId) {
        return enrollmentService.findByCourse(courseId);
    }
}
