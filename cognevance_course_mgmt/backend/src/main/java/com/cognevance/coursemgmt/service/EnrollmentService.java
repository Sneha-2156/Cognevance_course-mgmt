package com.cognevance.coursemgmt.service;

import com.cognevance.coursemgmt.dto.EnrollmentDtos.EnrollmentResponse;
import com.cognevance.coursemgmt.entity.Course;
import com.cognevance.coursemgmt.entity.Enrollment;
import com.cognevance.coursemgmt.entity.User;
import com.cognevance.coursemgmt.repository.CourseRepository;
import com.cognevance.coursemgmt.repository.EnrollmentRepository;
import com.cognevance.coursemgmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                              CourseRepository courseRepository,
                              UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public EnrollmentResponse enroll(String userEmail, Long courseId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new IllegalStateException("Already enrolled in this course");
        }

        Enrollment enrollment = enrollmentRepository.save(new Enrollment(user, course));
        return toResponse(enrollment);
    }

    public List<EnrollmentResponse> findByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return enrollmentRepository.findByUserId(user.getId())
                .stream().map(this::toResponse).toList();
    }

    public EnrollmentResponse updateProgress(String userEmail, Long courseId, int progress) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new IllegalArgumentException("Not enrolled in this course"));

        int clamped = Math.max(0, Math.min(100, progress));
        enrollment.setProgress(clamped);
        return toResponse(enrollmentRepository.save(enrollment));
    }

    // Admin view: everyone enrolled in a given course
    public List<Enrollment> findByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    private EnrollmentResponse toResponse(Enrollment e) {
        return new EnrollmentResponse(
                e.getId(), e.getCourse().getId(), e.getCourse().getTitle(),
                e.getCourse().getCategory(), e.getProgress());
    }
}
