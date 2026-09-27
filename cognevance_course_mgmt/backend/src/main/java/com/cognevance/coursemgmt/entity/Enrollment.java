package com.cognevance.coursemgmt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "course_id"}) // a student can enroll in a course only once
})
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // 0-100, updated as the student progresses through the course
    @Column(nullable = false)
    private int progress = 0;

    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt;

    @PrePersist
    protected void onCreate() {
        this.enrolledAt = LocalDateTime.now();
    }

    public Enrollment() {}

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
}
