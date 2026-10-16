CREATE DATABASE IF NOT EXISTS students_assignment;
USE students_assignment;

-- Create Students table
CREATE TABLE Students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(50),
    department VARCHAR(30),
    marks INT
);

-- Insert sample data for testing
INSERT INTO Students VALUES
(1, 'Arun', 'CSE', 85),
(2, 'Priya', 'CSE', 92),
(3, 'Rahul', 'CSE', 78),
(4, 'Sneha', 'CSE', 88),
(5, 'Kiran', 'CSE', 95),
(6, 'Meena', 'CSE', 72),
(7, 'Ravi', 'ECE', 68),
(8, 'Anu', 'ECE', 75),
(9, 'Vijay', 'ECE', 82),
(10, 'Divya', 'ECE', 91),
(11, 'Suresh', 'IT', 89),
(12, 'Neha', 'IT', 94),
(13, 'Manoj', 'IT', 76),
(14, 'Pooja', 'IT', 83),
(15, 'Ajay', 'IT', 70);


-- Q1. Find the average marks scored by students in each department.
SELECT
    department,
    AVG(marks) AS average_marks
FROM Students
GROUP BY department;


-- Q2. Find departments whose average marks are above 75.
SELECT
    department,
    AVG(marks) AS average_marks
FROM Students
GROUP BY department
HAVING AVG(marks) > 75;


-- Q3. Find the highest mark scored in each department.
SELECT
    department,
    MAX(marks) AS highest_mark
FROM Students
GROUP BY department;


-- Q4. Find the total number of students in each department.
SELECT
    department,
    COUNT(*) AS total_students
FROM Students
GROUP BY department;


-- Q5. Find departments having more than 5 students.
SELECT
    department,
    COUNT(*) AS total_students
FROM Students
GROUP BY department
HAVING COUNT(*) > 5;


-- Q6. Display departments sorted by average marks in descending order.
SELECT
    department,
    AVG(marks) AS average_marks
FROM Students
GROUP BY department
ORDER BY average_marks DESC;


-- Q7. Find the top 3 departments based on average marks.
SELECT
    department,
    AVG(marks) AS average_marks
FROM Students
GROUP BY department
ORDER BY average_marks DESC
LIMIT 3;


-- Q8. Find departments whose average marks are between 70 and 90.
SELECT
    department,
    AVG(marks) AS average_marks
FROM Students
GROUP BY department
HAVING AVG(marks) BETWEEN 70 AND 90;


-- Q9. Find the total marks scored by students in each department.
SELECT
    department,
    SUM(marks) AS total_marks
FROM Students
GROUP BY department;


-- Q10. Display departments sorted by the total number of students.
SELECT
    department,
    COUNT(*) AS total_students
FROM Students
GROUP BY department
ORDER BY total_students ASC;


-- Q11. Find the lowest mark scored in each department.
SELECT
    department,
    MIN(marks) AS lowest_mark
FROM Students
GROUP BY department;


-- Q12. Find departments where the highest mark is greater than 90.
SELECT
    department,
    MAX(marks) AS highest_mark
FROM Students
GROUP BY department
HAVING MAX(marks) > 90;


-- Q13. Find the number of students scoring above 80 in each department.
SELECT
    department,
    COUNT(*) AS students_above_80
FROM Students
WHERE marks > 80
GROUP BY department;


-- Q14. Find departments where more than 3 students scored above 75.
SELECT
    department,
    COUNT(*) AS students_above_75
FROM Students
WHERE marks > 75
GROUP BY department
HAVING COUNT(*) > 3;


-- Q15. Display departments ordered by highest mark in descending order.
SELECT
    department,
    MAX(marks) AS highest_mark
FROM Students
GROUP BY department
ORDER BY highest_mark DESC;