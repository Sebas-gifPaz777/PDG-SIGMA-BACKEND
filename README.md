# SIGMA — Backend (PDG-SIGMA-BACKEND)

SIGMA is the backend for a web application that simplifies the management of academic tutoring at Universidad Icesi. It supports publishing tutoring opportunities, managing applications, tracking activities and attendance, generating reports, and simulating integration with institutional platforms (SIMON and Banner).

Frontend: https://github.com/danielaolartebo/PDG-SIGMA

**Quick Start**

- Prerequisites: Java 17+, Git, network access to a PostgreSQL instance (or configure your datasource).
- Build and run (Linux/macOS):

```bash
./mvnw spring-boot:run
```

- Build and run (Windows PowerShell):

```powershell
.\\mvnw.cmd spring-boot:run
```

- Or build and run the JAR:

```bash
./mvnw package
java -jar target/*.jar
```

Configuration: application properties are under `src/main/resources` and environment-specific files exist in `src/main/resources` and `src/test/resources`. Common configuration keys to set via env or properties:
- `spring.datasource.*` (JDBC URL, username, password)
- `server.port`
- CORS origins (controllers currently allow `http://localhost:3000` and `https://pdg-sigma.vercel.app/`)

**API / Controllers (quick map)**

- Authentication: `POST /auth/login` — [src/main/java/com/pdg/sigma/controller/AuthController.java](src/main/java/com/pdg/sigma/controller/AuthController.java)
- Monitoring lifecycle, bulk upload, and reports: [src/main/java/com/pdg/sigma/controller/MonitoringController.java](src/main/java/com/pdg/sigma/controller/MonitoringController.java)
- Monitor applications and profiles: [src/main/java/com/pdg/sigma/controller/MonitorController.java](src/main/java/com/pdg/sigma/controller/MonitorController.java)
- Monitor–Monitoring relations and selection status: [src/main/java/com/pdg/sigma/controller/MonitoringMonitorController.java](src/main/java/com/pdg/sigma/controller/MonitoringMonitorController.java)
- Activities and attendance tracking: [src/main/java/com/pdg/sigma/controller/ActivityController.java](src/main/java/com/pdg/sigma/controller/ActivityController.java), [src/main/java/com/pdg/sigma/controller/AttendanceController.java](src/main/java/com/pdg/sigma/controller/AttendanceController.java)
- Batch email notifications for selection results: [src/main/java/com/pdg/sigma/controller/EmailSenderController.java](src/main/java/com/pdg/sigma/controller/EmailSenderController.java)
- Administrative endpoints (schools, programs, courses, categories, students, professors, department heads):
	- [SchoolController](src/main/java/com/pdg/sigma/controller/SchoolController.java)
	- [ProgramController](src/main/java/com/pdg/sigma/controller/ProgramController.java)
	- [CourseController](src/main/java/com/pdg/sigma/controller/CourseController.java)
	- [CategoryController](src/main/java/com/pdg/sigma/controller/CategoryController.java)
	- [StudentController](src/main/java/com/pdg/sigma/controller/StudentController.java)
	- [ProfessorController](src/main/java/com/pdg/sigma/controller/ProfessorController.java)
	- [DepartmentHeadController](src/main/java/com/pdg/sigma/controller/DepartmentHeadController.java)

- Debug/test helpers: [src/main/java/com/pdg/sigma/controller/DebugController.java](src/main/java/com/pdg/sigma/controller/DebugController.java)
- Data synchronization (SIMON/Banner simulator): `POST /api/sync/update` — [src/main/java/com/pdg/sigma/controller/DataSyncController.java](src/main/java/com/pdg/sigma/controller/DataSyncController.java)

**Design notes & recommendations**

- The controllers act as thin HTTP layers delegating to `*ServiceImpl` classes. DTOs are used for many payloads.
- CORS is enabled for the frontend origins, but there is limited authorization enforcement: consider adding token-based auth checking and role-based guards (`professor`, `monitor`, `jfedpto`) to sensitive endpoints.
- Error handling is inconsistent — standardize on a JSON error envelope and use DTO validation (`@Valid`) to improve client integration.
- Add explicit start/stop time-tracking or session endpoints so payments can be derived reliably from recorded hours rather than manual attendance only.
- `DataSyncController` and `DataSyncService` are the intended adapter layer for integrating institutional systems. Because SIMON currently lacks a public API, the project simulates integration — keep this layer pluggable so a real adapter can be swapped in later.

**Testing and Development**

- Important flows to test automatically: CSV bulk upload for monitorings, batch selection + email notifications (`/email-finish-selection`), attendance creation/deletion and reporting endpoints.
- Run unit and integration tests with Maven:

```bash
./mvnw test
```

**Next steps / Improvements**

- Implement RBAC enforcement and token validation middleware.
- Standardize API responses and add OpenAPI / Swagger documentation.
- Add scheduled reconciliation with the SIMON simulator and audit logging for selection/payment events.

---
SIGMA Backend — Universidad Icesi
