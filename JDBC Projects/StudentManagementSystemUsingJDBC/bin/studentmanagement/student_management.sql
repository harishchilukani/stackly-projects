CREATE DATABASE student_management_jdbc;

USE student_management_jdbc;

SHOW DATABASES;


USE student_management_jdbc;

CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15)
);

CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_name VARCHAR(100) NOT NULL,
    duration_months INT NOT NULL
);

CREATE TABLE enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrollment_date DATE NOT NULL,
    FOREIGN KEY (student_id)
        REFERENCES students(student_id),
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id)
);

CREATE TABLE attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    status ENUM('Present', 'Absent') NOT NULL,
    FOREIGN KEY (student_id)
        REFERENCES students(student_id),
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id)
);

CREATE TABLE marks (
    mark_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    marks_obtained DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (student_id)
        REFERENCES students(student_id),
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id)
);
