# JFS-Capstone-Project

This is platform for Java Full-Stack project

## ERD: Course Enrolment System

```
+-----------------------------------+             +-----------------------------------+             +-----------------------------------+
|               USERS               |             |            ENROLMENTS             |             |              COURSES              |
+-----------------------------------+             +-----------------------------------+             +-----------------------------------+
| PK | id         | String          | 1         N | PK | id         | String          | N         1 | PK | id         | String          |
| UK | email      | String (Unique) |-----------< | FK | userId     | String (USERS)  | >-----------| UK | courseCode | String (Unique) |
|    | name       | String          |  (places)   | FK | courseId   | String (COURSES)|  (contains) |    | title      | String          |
|    | password   | String (Hashed) |             |    | status     | ENROLLED/DROPPED|             |    | category   | String          |
|    | role       | ADMIN/STUDENT   |             |    | enrolledAt | DateTime        |             |    | level      | Beg/Inter/Adv   |
|    | createdAt  | DateTime        |             +-----------------------------------+             |    | capacity   | Integer         |
+-----------------------------------+             | UNIQUE KEY | (userId, courseId)   |             |    | status     | ACTIVE/INACTIVE |
                                                  +-----------------------------------+             |    | createdAt  | DateTime        |
                                                                                                    +-----------------------------------+
```

**Relationship Summary**

* **`USERS` -> `ENROLMENTS` (1 : N)**: One user can have multiple enrolment records. Each enrolment belongs to exactly one user.
* **`COURSES` -> `ENROLMENTS` (1 : N)**: One course can contain many student enrolments. Each enrolment belongs to one course.

**Business Rule Constraints**

* **No Duplicate Enrolments**: Enforced via unique compound index on `(userId, courseId)`.
* **Capacity Limit**: Application checks total active `ENROLMENTS` against `COURSES.capacity`.
* **Active Only**: Enrolment requires `COURSES.status` to be `ACTIVE`.