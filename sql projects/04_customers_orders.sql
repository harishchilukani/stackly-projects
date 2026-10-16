CREATE DATABASE IF NOT EXISTS customers_orders_assignment;
USE customers_orders_assignment;

-- Create Customers table
CREATE TABLE Customers (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(50),
    city VARCHAR(30)
);

-- Create Orders table
CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    amount DECIMAL(10,2),
    order_date DATE,
    FOREIGN KEY (customer_id)
        REFERENCES Customers(customer_id)
);

-- Insert sample customers
INSERT INTO Customers VALUES
(1, 'Arun', 'Chennai'),
(2, 'Priya', 'Hyderabad'),
(3, 'Rahul', 'Bangalore'),
(4, 'Sneha', 'Mumbai'),
(5, 'Kiran', 'Delhi');

-- Insert sample orders
INSERT INTO Orders VALUES
(101, 1, 3000, '2026-01-05'),
(102, 1, 2500, '2026-01-10'),
(103, 1, 4000, '2026-02-15'),
(104, 1, 1500, '2026-03-01'),
(105, 2, 5000, '2026-01-12'),
(106, 2, 3500, '2026-02-20'),
(107, 2, 2500, '2026-03-15'),
(108, 3, 1800, '2026-01-20'),
(109, 3, 2200, '2026-02-25'),
(110, 4, 6000, '2026-03-05');


-- Q1. Find the total order amount for each customer.
SELECT
    c.customer_id,
    c.customer_name,
    COALESCE(SUM(o.amount), 0) AS total_order_amount
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;


-- Q2. Find customers who have placed more than 3 orders.
SELECT
    c.customer_id,
    c.customer_name,
    COUNT(o.order_id) AS total_orders
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) > 3;


-- Q3. Find the average order amount for each customer.
SELECT
    c.customer_id,
    c.customer_name,
    AVG(o.amount) AS average_order_amount
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;


-- Q4. Find the highest order amount placed by each customer.
SELECT
    c.customer_id,
    c.customer_name,
    MAX(o.amount) AS highest_order_amount
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;


-- Q5. Display customers sorted by their total purchase amount.
SELECT
    c.customer_id,
    c.customer_name,
    COALESCE(SUM(o.amount), 0) AS total_purchase_amount
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase_amount DESC;


-- Q6. Find customers whose total purchase amount exceeds 10,000.
SELECT
    c.customer_id,
    c.customer_name,
    SUM(o.amount) AS total_purchase_amount
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.amount) > 10000;


-- Q7. Display customer names with the total number of orders.
SELECT
    c.customer_id,
    c.customer_name,
    COUNT(o.order_id) AS total_orders
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;


-- Q8. Find the customer(s) who spent the highest total amount.
SELECT
    c.customer_id,
    c.customer_name,
    SUM(o.amount) AS total_purchase_amount
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.amount) = (
    SELECT MAX(customer_total)
    FROM (
        SELECT SUM(amount) AS customer_total
        FROM Orders
        GROUP BY customer_id
    ) AS totals
);


-- Q9. Find the customer(s) who placed the maximum number of orders.
SELECT
    c.customer_id,
    c.customer_name,
    COUNT(o.order_id) AS total_orders
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) = (
    SELECT MAX(order_count)
    FROM (
        SELECT COUNT(*) AS order_count
        FROM Orders
        GROUP BY customer_id
    ) AS counts
);


-- Q10. Find customers whose average order amount is greater than 2,000.
SELECT
    c.customer_id,
    c.customer_name,
    AVG(o.amount) AS average_order_amount
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING AVG(o.amount) > 2000;


-- Q11. Display the top 5 customers based on total purchase amount.
SELECT
    c.customer_id,
    c.customer_name,
    COALESCE(SUM(o.amount), 0) AS total_purchase_amount
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase_amount DESC, c.customer_id ASC
LIMIT 5;


-- Q12. Find the minimum order amount for each customer.
SELECT
    c.customer_id,
    c.customer_name,
    MIN(o.amount) AS minimum_order_amount
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;


-- Q13. Find customers whose total purchase amount exceeds 5,000.
SELECT
    c.customer_id,
    c.customer_name,
    SUM(o.amount) AS total_purchase_amount
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.amount) > 5000;


-- Q14. Display customer-wise total orders and total purchase amount.
SELECT
    c.customer_id,
    c.customer_name,
    COUNT(o.order_id) AS total_orders,
    COALESCE(SUM(o.amount), 0) AS total_purchase_amount
FROM Customers c
LEFT JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;


-- Q15. Find customers who placed more than 2 orders
-- and spent more than 8,000.
SELECT
    c.customer_id,
    c.customer_name,
    COUNT(o.order_id) AS total_orders,
    SUM(o.amount) AS total_purchase_amount
FROM Customers c
INNER JOIN Orders o
    ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) > 2
   AND SUM(o.amount) > 8000;