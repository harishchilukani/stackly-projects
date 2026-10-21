package bankingmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountService {

    // Read and validate a positive customer ID
    private int readCustomerId(Scanner sc) {
        while (true) {
            System.out.print("Enter customer ID: ");
            String input = sc.nextLine().trim();

            try {
                int customerId = Integer.parseInt(input);

                if (customerId <= 0) {
                    System.out.println(
                            "Error: Customer ID must be greater than zero.");
                } else {
                    return customerId;
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: Please enter a valid numeric customer ID.");
            }
        }
    }

    // Create a new bank account
    public void createAccount(Scanner sc) {

        int customerId = readCustomerId(sc);

        String accountType;

        while (true) {
            System.out.print(
                    "Enter account type (Savings/Current): ");
            accountType = sc.nextLine().trim();

            if (accountType.equalsIgnoreCase("Savings")) {
                accountType = "Savings";
                break;
            } else if (accountType.equalsIgnoreCase("Current")) {
                accountType = "Current";
                break;
            } else {
                System.out.println(
                        "Error: Account type must be Savings or Current.");
            }
        }

        String customerSql =
                "SELECT customer_id FROM customers WHERE customer_id = ?";

        String insertSql =
                "INSERT INTO accounts "
                        + "(customer_id, account_type, balance) "
                        + "VALUES (?, ?, 0.00)";

        try (Connection con = DBConnection.getConnection()) {

            // Check whether the customer exists
            try (PreparedStatement check =
                         con.prepareStatement(customerSql)) {

                check.setInt(1, customerId);

                try (ResultSet rs = check.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println(
                                "Error: Customer ID does not exist. "
                                        + "Please register the customer first.");
                        return;
                    }
                }
            }

            // Create the account with an initial balance of zero
            try (PreparedStatement ps =
                         con.prepareStatement(insertSql)) {

                ps.setInt(1, customerId);
                ps.setString(2, accountType);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Bank account created successfully!");
                } else {
                    System.out.println(
                            "Account could not be created.");
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Account creation failed: " + e.getMessage());
        }
    }

    // View all bank accounts
    public void viewAccounts() {

        String sql =
                "SELECT a.account_id, a.customer_id, "
                        + "c.customer_name, a.account_type, a.balance "
                        + "FROM accounts a "
                        + "JOIN customers c "
                        + "ON a.customer_id = c.customer_id "
                        + "ORDER BY a.account_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Bank Account Details ---");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.printf(
                        "Account ID: %d | Customer ID: %d | "
                                + "Name: %s | Type: %s | Balance: %.2f%n",
                        rs.getInt("account_id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("account_type"),
                        rs.getBigDecimal("balance"));
            }

            if (!found) {
                System.out.println("No bank accounts found.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to view accounts: " + e.getMessage());
        }
    }
}