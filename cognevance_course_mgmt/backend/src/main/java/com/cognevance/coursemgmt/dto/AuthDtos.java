package com.cognevance.coursemgmt.dto;

import com.cognevance.coursemgmt.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public static class RegisterRequest {
        @NotBlank
        public String name;

        @NotBlank @Email
        public String email;

        @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
        public String password;

        // Optional - defaults to STUDENT if omitted; only used so an admin account can be seeded
        public Role role;
    }

    public static class LoginRequest {
        @NotBlank @Email
        public String email;

        @NotBlank
        public String password;
    }

    public static class AuthResponse {
        public String token;
        public String name;
        public String email;
        public Role role;

        public AuthResponse(String token, String name, String email, Role role) {
            this.token = token;
            this.name = name;
            this.email = email;
            this.role = role;
        }
    }
}
