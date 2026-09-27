# Cognevance Project 2 — Online Course Management System

A full-stack app for managing online courses and student enrollments: JWT
authentication with student/admin roles, a course catalog with search and
category filtering, an admin dashboard for course CRUD, and a student
dashboard for tracking per-course progress.

## Tech Stack
- **Frontend:** React 18 (Vite) + React Router
- **Backend:** Java 17, Spring Boot 3 (Web, Security, Data JPA, Validation), JWT (jjwt)
- **Database:** MySQL
- **Deployment targets:** Vercel/Netlify (frontend), Render/Railway (backend + MySQL)

## Project Structure
```
cognevance_course_mgmt/
├── frontend/
│   └── src/
│       ├── api/client.js        # fetch wrapper, attaches JWT
│       ├── context/AuthContext  # login/register/logout state
│       ├── components/          # Navbar, ProtectedRoute
│       └── pages/               # Login, Register, Courses, Dashboard, Admin
└── backend/
    └── src/main/java/com/cognevance/coursemgmt/
        ├── entity/       (User, Course, Enrollment, Role)
        ├── repository/   (Spring Data JPA repos)
        ├── security/     (JwtUtil, JwtAuthFilter, AppUserDetailsService)
        ├── service/      (AuthService, CourseService, EnrollmentService)
        ├── controller/   (AuthController, CourseController, EnrollmentController)
        ├── dto/          (request/response payloads)
        ├── config/       (SecurityConfig)
        └── exception/    (GlobalExceptionHandler)
```

## How Authentication Works
1. `POST /api/auth/register` or `/api/auth/login` returns a JWT + user info.
2. The frontend stores the token in `localStorage` and attaches it as
   `Authorization: Bearer <token>` on every request (see `api/client.js`).
3. `JwtAuthFilter` validates the token on each request and sets the
   authenticated user; `SecurityConfig` enforces role rules (e.g. only
   `ADMIN` can create/edit/delete courses).

## Backend Setup
1. Install Java 17+ and have MySQL running locally.
2. In `backend/src/main/resources/application.properties`, set your MySQL
   credentials and a real `jwt.secret` (or export `DB_USERNAME`,
   `DB_PASSWORD`, `JWT_SECRET` env vars — the file already falls back to those).
3. Run:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
   API starts on `http://localhost:8080`. Tables are auto-created.
4. To get an admin account, register normally then either update that
   user's `role` to `ADMIN` directly in MySQL, or include `"role": "ADMIN"`
   in the register request body (fine for dev; remove that option in a
   real production build so anyone can't self-promote to admin).

## Frontend Setup
```bash
cd frontend
npm install
npm run dev
```
Opens on `http://localhost:5173`. `/api/*` is proxied to `localhost:8080` in dev.

## API Endpoints
| Method | Endpoint                        | Access          | Description                     |
|--------|----------------------------------|-----------------|----------------------------------|
| POST   | `/api/auth/register`            | Public          | Create account, returns JWT      |
| POST   | `/api/auth/login`               | Public          | Login, returns JWT               |
| GET    | `/api/courses`                  | Public          | List/search/filter courses       |
| GET    | `/api/courses/{id}`             | Public          | Get one course                   |
| POST   | `/api/courses`                  | Admin           | Create course                    |
| PUT    | `/api/courses/{id}`             | Admin           | Update course                    |
| DELETE | `/api/courses/{id}`             | Admin           | Delete course                    |
| POST   | `/api/enrollments/{courseId}`   | Authenticated   | Enroll in a course                |
| GET    | `/api/enrollments/me`           | Authenticated   | My enrollments + progress         |
| PUT    | `/api/enrollments/{id}/progress`| Authenticated   | Update my progress (0-100)        |
| GET    | `/api/enrollments/course/{id}`  | Authenticated   | Admin: who's enrolled in a course |

## Deployment
- **Frontend:** deploy `frontend/`, set `VITE_API_BASE` to your deployed
  backend's `/api` URL.
- **Backend:** deploy `backend/` as a Java/Maven service on Render/Railway
  with a MySQL add-on; set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`.
- Restrict CORS in `SecurityConfig.java` from `*` to your real frontend
  domain before going live
