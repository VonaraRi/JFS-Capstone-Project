# JFS-Capstone-Project

Full-stack Java application for course registration and enrolment management.

---
## 🚀 Setup and Run Instructions

### Prerequisites
* Java JDK 17 or higher
* Node.js (v18+) and `npm`
* MongoDB server running locally (or connection URL configured in `application.properties`)

---

### Step 1: Start Backend (Spring Boot)

Open your first terminal window at the project root directory and run:

```bash
mvn spring-boot:run
```
---

### Step 2: Start Frontend (React + Vite)

Open a second terminal window, navigate to the UI directory, and start the Vite Development server:

```bash
cd frontend/course-enrolment-ui
npm run dev
```

---

### 🎯 1. Problem Statement

* **Data Integrity**: Prevents duplicate course registrations.
* **Registration Mistakes**: Prevents students from signing up for full or inactive classes.
* **Manual Tracking**: Replaces manual lists with real-time seat tracking and analytics reports.

---

### 🏗️ 2. Architecture & Tech Stack

* **Frontend**: React (Vite) + CSS.
* **Backend**: Java Spring Boot (RESTful API + JWT Auth).
* **Database**: MongoDB (`users`, `courses`, `enrolments`).

---

### 🔄 3. Main User Flow

* **Student**: Register ➔ Login ➔ Browse Active Courses ➔ Enroll / Drop ➔ View Personal Registered courses.
* **Admin**: Login ➔ Manage Courses (Create, Edit Capacity/Status) ➔ Access aggregrated Analytics Reports.

---

### 💻 4 & 5. Live Demonstrations

* **Admin Dashboard**: Course management and analytics reporting.
* **Student View**: Course browsing, enrolment, dropping, and profile status.

---

### 🗄️ 6. MongoDB Model

#### Collections
* **`users`**: Credentials, roles (`STUDENT`, `ADMIN`), profile details.
* **`courses`**: Details, code, capacity, status (`ACTIVE`, `INACTIVE`).
* **`enrolments`**: Links `userId` & `courseId` with status (`ENROLLED`, `DROPPED`).

#### ERD Diagram

```
+-----------------------------------+             +-----------------------------------+             +-----------------------------------+
|               USERS               |             |            ENROLMENTS             |             |              COURSES              |
+-----------------------------------+             +-----------------------------------+             +-----------------------------------+
| PK | id         | String          | 1         N | PK | id         | String          | N         1 | PK | id         | String          |
|    | email      | String (Unique) |-----------< | FK | userId     | String (USERS)  |             |    | courseCode | String (Unique) |
|    | name       | String          |  (places)   | FK | courseId   | String (COURSES)| >-----------|    | title      | String          |
|    | password   | String (Hashed) |             |    | status     | ENROLLED/DROPPED|  (contains) |    | category   | String          |
|    | role       | ADMIN/STUDENT   |             |    | enrolledAt | DateTime        |             |    | level      | Beg/Inter/Adv   |
|    | createdAt  | DateTime        |             +-----------------------------------+             |    | capacity   | Integer         |
+-----------------------------------+             | UNIQUE KEY | (userId, courseId)   |             |    | status     | ACTIVE/INACTIVE |
                                                  +-----------------------------------+             |    | createdAt  | DateTime        |
                                                                                                    +-----------------------------------+
```

---

### 📊 7. Aggregation Reporting Pipeline (Most Popular Courses)

1. **Match**: Filter active enrolments (`ENROLLED` / `ACTIVE`).
2. **Type Conversion**: Convert `courseId` and `userId` to ObjectIDs.
3. **Lookup**: Join with `courses` and `users` collections.
4. **Group**: Group by course title and collect unique student names (`$addToSet`).
5. **Project & Sort**: Count unique active students, sort descending, limit to Top 3.

---

### 🛡️ 8. Validation or Business Rule ("Student cannot enroll in inactive courses")

* **Frontend Guard**: Filters out inactive courses in `StudentRegisterCoursePage.jsx`:
  ```javascript
  .filter(course => course.status.toUpperCase() !== 'INACTIVE')

* **Backend Gurd**: EnrolmentService.java validates course status before saving to DB.

---

### 🔐 9. Protected Route / Role-Based Access Control (RBAC)

* **Documentation**: View route rules at /api/docs.

* **Admin-Only Routes**: /api/v1/reports/** and course creation restricted to ADMIN.

* **Security Action**: Spring Security returns 403 Forbidden if a student accesses admin routes.

---

### ⚠️ 10. Challenge & Solution

* **Challenge**: Historical DROPPED records returned during fetches, causing UI bugs.

* **Solution**: Applied status filtering in StudentRegisterCoursePage.jsx:
  ```javascript
  const activeEnrolments = (enrolledData || []).filter(item => item.status === 'ENROLLED');

* **Result**: Displays active enrolments clearly without history corrupting the UI.

---
