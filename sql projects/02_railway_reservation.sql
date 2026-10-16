CREATE DATABASE IF NOT EXISTS railway_reservation_assignment;
USE railway_reservation_assignment;

-- Create Trains table
CREATE TABLE trains (
    train_id INT PRIMARY KEY AUTO_INCREMENT,
    train_name VARCHAR(50),
    source VARCHAR(50),
    destination VARCHAR(50)
);

-- Create Bookings table
CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    train_id INT,
    passenger_name VARCHAR(50),
    fare DECIMAL(10,2),
    status VARCHAR(20),
    FOREIGN KEY (train_id) REFERENCES trains(train_id)
);

-- Insert sample data into Trains
INSERT INTO trains (train_name, source, destination)
VALUES
('Express1', 'Chennai', 'Madurai'),
('Express2', 'Coimbatore', 'Salem'),
('Express3', 'Madurai', 'Chennai');

-- Insert sample data into Bookings
INSERT INTO bookings
(train_id, passenger_name, fare, status)
VALUES
(1, 'Janani', 500, 'Confirmed'),
(1, 'Arun', 500, 'Waiting'),
(2, 'Priya', 300, 'Confirmed'),
(3, 'Karthik', 450, 'Cancelled'),
(2, 'Meena', 300, 'Confirmed');


-- ============================================
-- BASIC + WHERE + OPERATORS
-- ============================================

-- Q41. Retrieve all bookings with fare greater than 400.
SELECT *
FROM bookings
WHERE fare > 400;


-- Q42. Find bookings where status is not 'Confirmed'.
SELECT *
FROM bookings
WHERE status <> 'Confirmed';


-- Q43. Display trains starting from Chennai.
SELECT *
FROM trains
WHERE source = 'Chennai';


-- Q44. Retrieve bookings with fare between 300 and 500.
SELECT *
FROM bookings
WHERE fare BETWEEN 300 AND 500;


-- Q45. Find passengers whose names start with 'A'.
SELECT *
FROM bookings
WHERE passenger_name LIKE 'A%';


-- ============================================
-- JOINS + GROUP BY + ORDER BY
-- ============================================

-- Q46. Retrieve train names along with passenger names.
SELECT
    t.train_name,
    b.passenger_name
FROM trains AS t
INNER JOIN bookings AS b
    ON t.train_id = b.train_id;


-- Q47. Count number of bookings for each train.
SELECT
    t.train_id,
    t.train_name,
    COUNT(b.booking_id) AS booking_count
FROM trains AS t
LEFT JOIN bookings AS b
    ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name
ORDER BY t.train_id;


-- Q48. Display train names and total fare collected for each train.
-- Only Confirmed bookings are counted as collected fares.
SELECT
    t.train_id,
    t.train_name,
    COALESCE(
        SUM(
            CASE
                WHEN b.status = 'Confirmed' THEN b.fare
                ELSE 0
            END
        ),
        0
    ) AS total_fare_collected
FROM trains AS t
LEFT JOIN bookings AS b
    ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name
ORDER BY t.train_id;


-- ============================================
-- SUBQUERIES
-- ============================================

-- Q49. Find bookings with fare equal to the highest fare.
SELECT *
FROM bookings
WHERE fare = (
    SELECT MAX(fare)
    FROM bookings
);


-- Q50. Retrieve trains that have more than one booking.
SELECT
    t.train_id,
    t.train_name
FROM trains AS t
WHERE (
    SELECT COUNT(*)
    FROM bookings AS b
    WHERE b.train_id = t.train_id
) > 1;