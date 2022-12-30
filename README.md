### Java Servlet Final Project
This project is a final project for EPAM Java Summer 2022 Course

There is a list of courses divided into topics. One teacher is assigned to each course. It is necessary to implement the following functionality:
> sort courses by name (az, za), duration, number of students enrolled in the course;

> a selection of courses related to a specific topic;

> a selection of the teacher courses.
  
The student enrolls in one or more courses, registration data is stored. At the end of the course the teacher gives the student a grade, which is stored in the journal.

Each user has a personal account, which displays brief information about the user, as well as

For the student:
- a list of courses for which the student has registered but which have not yet begun;
- a list of courses for which the student has registered and which are in progress;
- a list of completed courses with information about grades;
  
For the teacher:
- viewing and editing an e-journal for assigned courses.
  The system administrator has the rights:
- registration of the teacher and assignment of the course to him;
- adding, deleting, editing a course;
- blocking, unlocking the student.

Info about database related stuff you can find in (creation script and db scheme)
> [/src/resources/db](src/main/resources/db)

DB Schema

![db schema image](src/main/resources/db/db_scheme_new.png)
