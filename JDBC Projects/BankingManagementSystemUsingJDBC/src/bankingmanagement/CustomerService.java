
package bankingmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerService {

    public void registerCustomer(Scanner sc) {

        String name;

        while (true) {
            System.out.print("Enter customer name: ");
            name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Error: Name cannot be empty.");
            } else if (!name.matches("[a-zA-Z ]+")) {
                System.out.println(
                        "Error: Name must contain only letters and spaces.");
            } else {
                break;
            }
        }

        String email;

        while (true) {
            System.out.print("Enter email: ");
            email = sc.nextLine().trim();

            if (!email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.out.println(
                        "Error: Please enter a valid email address.");
            } else {
                break;
            }
        }

        String phone;

        while (true) {
            System.out.print("Enter phone number: ");
            phone = sc.nextLine().trim();

            if (!phone.matches("[0-9]{10}")) {
                System.out.println(
                        "Error: Phone number must contain exactly 10 digits.");
            } else {
                break;
            }
        }

        String city;

        while (true) {
            System.out.print("Enter city: ");
            city = sc.nextLine().trim();

            if (city.isEmpty()) {
                System.out.println("Error: City cannot be empty.");
            } else {
                break;
            }
        }

        String sql = "INSERT INTO customers "
                + "(customer_name, email, phone, city) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, city);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(
                        "Customer registered successfully!");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Customer registration failed: " + e.getMessage());
        }
    }

    public void viewCustomers() {

        String sql = "SELECT * FROM customers ORDER BY customer_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Customer Details ---");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println(
                        "Customer ID: " + rs.getInt("customer_id")
                        + " | Name: " + rs.getString("customer_name")
                        + " | Email: " + rs.getString("email")
                        + " | Phone: " + rs.getString("phone")
                        + " | City: " + rs.getString("city"));
            }

            if (!found) {
                System.out.println("No customers found.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Unable to view customers: " + e.getMessage());
        }
    }
}
