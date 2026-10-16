CREATE DATABASE IF NOT EXISTS employee_management_assignment;
USE employee_management_assignment;

-- Create Employee table
CREATE TABLE Employee (
    emp_id INT PRIMARY KEY,
    emp_name VARCHAR(50),
    department VARCHAR(30),
    salary DECIMAL(10,2),
    city VARCHAR(30),
    joining_date DATE
);

-- Insert sample data
INSERT INTO Employee VALUES
(101, 'John', 'IT', 60000, 'Chennai', '2022-01-15'),
(102, 'David', 'HR', 45000, 'Bangalore', '2021-03-10'),
(103, 'Smith', 'IT', 70000, 'Chennai', '2020-07-12'),
(104, 'Mary', 'Finance', 55000, 'Mumbai', '2023-01-20'),
(105, 'James', 'HR', 48000, 'Delhi', '2022-05-05'),
(106, 'Linda', 'Finance', 65000, 'Mumbai', '2021-08-18');


-- Q1. Find the total number of employees in each department.
SELECT
    department,
    COUNT(*) AS total_employees
FROM Employee
GROUP BY department;


-- Q2. Find the average salary of employees in each department.
SELECT
    department,
    AVG(salary) AS average_salary
FROM Employee
GROUP BY department;


-- Q3. Display departments having more than one employee.
SELECT
    department,
    COUNT(*) AS total_employees
FROM Employee
GROUP BY department
HAVING COUNT(*) > 1;


-- Q4. Find the highest salary in each department.
SELECT
    department,
    MAX(salary) AS highest_salary
FROM Employee
GROUP BY department;


-- Q5. Find the lowest salary in each department.
SELECT
    department,
    MIN(salary) AS lowest_salary
FROM Employee
GROUP BY department;


-- Q6. Find departments whose average salary is greater than 50,000.
SELECT
    department,
    AVG(salary) AS average_salary
FROM Employee
GROUP BY department
HAVING AVG(salary) > 50000;


-- Q7. Calculate the total salary expenditure for each department.
SELECT
    department,
    SUM(salary) AS total_salary
FROM Employee
GROUP BY department;


-- Q8. Display all employees sorted by salary in descending order.
SELECT *
FROM Employee
ORDER BY salary DESC;


-- Q9. Display employees sorted first by department
-- and then by salary in descending order.
SELECT *
FROM Employee
ORDER BY department ASC, salary DESC;


-- Q10. Find cities that have more than one employee.
SELECT
    city,
    COUNT(*) AS total_employees
FROM Employee
GROUP BY city
HAVING COUNT(*) > 1;


-- Q11. Find the total salary paid in each city.
SELECT
    city,
    SUM(salary) AS total_salary
FROM Employee
GROUP BY city;


-- Q12. Display departments ordered by total salary
-- expenditure from highest to lowest.
SELECT
    department,
    SUM(salary) AS total_salary
FROM Employee
GROUP BY department
ORDER BY total_salary DESC;


-- Q13. Find the number of employees in each department
-- whose salary is greater than 50,000.
SELECT
    department,
    COUNT(*) AS employee_count
FROM Employee
WHERE salary > 50000
GROUP BY department;


-- Q14. Find the difference between the highest and
-- lowest salary in each department.
SELECT
    department,
    MAX(salary) - MIN(salary) AS salary_difference
FROM Employee
GROUP BY department;


-- Q15. Display the top 3 highest-paid employees.
SELECT *
FROM Employee
ORDER BY salary DESC
LIMIT 3;