CREATE DATABASE IF NOT EXISTS banking_assignment;
USE banking_assignment;

CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    city VARCHAR(50)
);

CREATE TABLE accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    account_type VARCHAR(20),
    balance DECIMAL(10,2),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

INSERT INTO customers (name, city) VALUES
('Janani','Chennai'),
('Arun','Coimbatore'),
('Priya','Madurai'),
('Karthik','Salem');

INSERT INTO accounts (customer_id, account_type, balance) VALUES
(1,'Savings',50000),
(1,'Current',20000),
(2,'Savings',30000),
(3,'Savings',15000),
(4,'Current',40000);

-- Q1. Accounts with balance greater than 20,000
SELECT * FROM accounts WHERE balance > 20000;

-- Q2. Customers living in Chennai
SELECT * FROM customers WHERE city = 'Chennai';

-- Q3. Balance between 20,000 and 50,000
SELECT * FROM accounts WHERE balance BETWEEN 20000 AND 50000;

-- Q4. Names starting with J
SELECT * FROM customers WHERE name LIKE 'J%';

-- Q5. Savings or Current accounts
SELECT * FROM accounts
WHERE account_type IN ('Savings','Current');

-- Q6. Accounts that are not Savings
SELECT * FROM accounts WHERE account_type <> 'Savings';

-- Q7. Names containing the letter a
SELECT * FROM customers WHERE name LIKE '%a%';

-- Q8. Balance less than or equal to 30,000
SELECT * FROM accounts WHERE balance <= 30000;

-- Q9. Customers not from Madurai
SELECT * FROM customers WHERE city <> 'Madurai';

-- Q10. Balance not between 10,000 and 40,000
SELECT * FROM accounts
WHERE balance NOT BETWEEN 10000 AND 40000;

-- Q11. Names ending with i
SELECT * FROM customers WHERE name LIKE '%i';

-- Q12. Balance equal to 50,000
SELECT * FROM accounts WHERE balance = 50000;

-- Q13. Customers from Chennai or Salem
SELECT * FROM customers
WHERE city IN ('Chennai','Salem');

-- Q14. Balance greater than 10,000 and less than 40,000
SELECT * FROM accounts
WHERE balance > 10000 AND balance < 40000;

-- Q15. Accounts that are not Current
SELECT * FROM accounts
WHERE account_type NOT IN ('Current');

-- Q16. Accounts sorted by balance descending
SELECT * FROM accounts ORDER BY balance DESC;

-- Q17. Customers sorted alphabetically
SELECT * FROM customers ORDER BY name ASC;

-- Q18. Account type ascending, balance descending
SELECT * FROM accounts
ORDER BY account_type ASC, balance DESC;

-- Q19. Total balance
SELECT SUM(balance) AS total_balance FROM accounts;

-- Q20. Average balance
SELECT AVG(balance) AS average_balance FROM accounts;

-- Q21. Maximum balance
SELECT MAX(balance) AS maximum_balance FROM accounts;

-- Q22. Minimum balance
SELECT MIN(balance) AS minimum_balance FROM accounts;

-- Q23. Total number of customers
SELECT COUNT(*) AS total_customers FROM customers;

-- Q24. Total balance by account type
SELECT account_type, SUM(balance) AS total_balance
FROM accounts
GROUP BY account_type;

-- Q25. Average balance by account type
SELECT account_type, AVG(balance) AS average_balance
FROM accounts
GROUP BY account_type;

-- Q26. Account types with average balance greater than 20,000
SELECT account_type, AVG(balance) AS average_balance
FROM accounts
GROUP BY account_type
HAVING AVG(balance) > 20000;

-- Q27. Number of accounts for each customer
SELECT c.customer_id, c.name, COUNT(a.account_id) AS account_count
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name;

-- Q28. Customers having more than one account
SELECT c.customer_id, c.name, COUNT(a.account_id) AS account_count
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name
HAVING COUNT(a.account_id) > 1;

-- Q29. Customer names with account balances
SELECT c.name, a.balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id;

-- Q30. All customers and their accounts
SELECT c.customer_id, c.name, c.city,
       a.account_id, a.account_type, a.balance
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id;

-- Q31. All accounts with customer details
SELECT a.account_id, a.account_type, a.balance,
       c.customer_id, c.name, c.city
FROM accounts a
LEFT JOIN customers c ON a.customer_id = c.customer_id;

-- Q32. Customer names and account types with balance > 20,000
SELECT c.name, a.account_type, a.balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
WHERE a.balance > 20000;

-- Q33. Total balance for each customer
SELECT c.customer_id, c.name,
       COALESCE(SUM(a.balance), 0) AS total_balance
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name;

-- Q34. Customer names and balances sorted by balance
SELECT c.name, a.balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
ORDER BY a.balance ASC;

-- Q35. Number of accounts for each city
SELECT c.city, COUNT(a.account_id) AS account_count
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.city;

-- Q36. Accounts with balance above average
SELECT * FROM accounts
WHERE balance > (SELECT AVG(balance) FROM accounts);

-- Q37. Customers who have accounts
SELECT * FROM customers c
WHERE EXISTS (
    SELECT 1 FROM accounts a
    WHERE a.customer_id = c.customer_id
);

-- Q38. Customers without accounts
SELECT * FROM customers c
WHERE NOT EXISTS (
    SELECT 1 FROM accounts a
    WHERE a.customer_id = c.customer_id
);

-- Q39. Account(s) with maximum balance
SELECT * FROM accounts
WHERE balance = (SELECT MAX(balance) FROM accounts);

-- Q40. Customers whose total balance exceeds 40,000
SELECT c.customer_id, c.name
FROM customers c
WHERE (
    SELECT COALESCE(SUM(a.balance), 0)
    FROM accounts a
    WHERE a.customer_id = c.customer_id
) > 40000;