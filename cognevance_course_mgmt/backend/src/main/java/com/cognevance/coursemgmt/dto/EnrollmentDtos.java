package com.cognevance.coursemgmt.dto;

public class EnrollmentDtos {

    public static class ProgressUpdateRequest {
        public int progress; // 0-100
    }

    public static class EnrollmentResponse {
        public Long enrollmentId;
        public Long courseId;
        public String courseTitle;
        public String category;
        public int progress;

        public EnrollmentResponse(Long enrollmentId, Long courseId, String courseTitle, String category, int progress) {
            this.enrollmentId = enrollmentId;
            this.courseId = courseId;
            this.courseTitle = courseTitle;
            this.category = category;
            this.progress = progress;
        }
    }
}
