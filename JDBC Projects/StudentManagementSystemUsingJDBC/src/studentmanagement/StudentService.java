
package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentService {

    // Register student
    public void registerStudent(Scanner sc) {

        String name;

        while (true) {
            System.out.print("Enter student name: ");
            name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Error: Name cannot be empty.");
            } else if (!name.matches("[\\p{L} .'-]+")) {
                System.out.println("Error: Enter a valid student name.");
            } else {
                break;
            }
        }

        String email;

        while (true) {
            System.out.print("Enter student email: ");
            email = sc.nextLine().trim();

            if (!email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.out.println("Error: Enter a valid email address.");
            } else {
                break;
            }
        }

        String phone;

        while (true) {
            System.out.print("Enter student phone: ");
            phone = sc.nextLine().trim();

            if (!phone.matches("[0-9]{10}")) {
                System.out.println(
                        "Error: Phone number must contain exactly 10 digits.");
            } else {
                break;
            }
        }

        String sql =
                "INSERT INTO students (name, email, phone) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student registered successfully!");
            } else {
                System.out.println("Student registration failed.");
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                System.out.println(
                        "Registration failed: Email already exists "
                                + "or a database constraint was violated.");
            } else {
                System.out.println(
                        "Database error during registration: "
                                + e.getMessage());
            }
        }
    }

    // View all students
    public void viewAllStudents() {

        String sql = "SELECT * FROM students ORDER BY student_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n===== STUDENT RECORDS =====");

            while (rs.next()) {
                found = true;

                System.out.println(
                        "ID: " + rs.getInt("student_id")
                        + " | Name: " + rs.getString("name")
                        + " | Email: " + rs.getString("email")
                        + " | Phone: " + rs.getString("phone"));
            }

            if (!found) {
                System.out.println("No student records found.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Database error while viewing students: "
                            + e.getMessage());
        }
    }

    // Update student
    public void updateStudent(Scanner sc) {

        int id;

        while (true) {
            System.out.print("Enter student ID to update: ");
            String input = sc.nextLine().trim();

            try {
                id = Integer.parseInt(input);

                if (id <= 0) {
                    System.out.println(
                            "Error: Student ID must be greater than zero.");
                } else {
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: Enter a valid numeric student ID.");
            }
        }

        System.out.println("\n===== UPDATE STUDENT =====");
        System.out.println("1. Update Name");
        System.out.println("2. Update Email");
        System.out.println("3. Update Phone");
        System.out.println("4. Update All Details");
        System.out.println("5. Cancel");

        int choice;

        while (true) {
            System.out.print("Enter your choice: ");
            String input = sc.nextLine().trim();

            try {
                choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= 5) {
                    break;
                }

                System.out.println(
                        "Invalid choice. Enter a number from 1 to 5.");

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: Enter a valid numeric choice.");
            }
        }

        if (choice == 5) {
            System.out.println("Update cancelled.");
            return;
        }

        String sql;
        String value1 = "";
        String value2 = "";
        String value3 = "";

        switch (choice) {

            case 1:
                while (true) {
                    System.out.print("Enter new name: ");
                    value1 = sc.nextLine().trim();

                    if (value1.isEmpty()) {
                        System.out.println("Name cannot be empty.");
                    } else if (!value1.matches("[\\p{L} .'-]+")) {
                        System.out.println("Enter a valid student name.");
                    } else {
                        break;
                    }
                }

                sql = "UPDATE students SET name = ? WHERE student_id = ?";
                break;

            case 2:
                while (true) {
                    System.out.print("Enter new email: ");
                    value1 = sc.nextLine().trim();

                    if (!value1.matches(
                            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                        System.out.println("Enter a valid email address.");
                    } else {
                        break;
                    }
                }

                sql = "UPDATE students SET email = ? WHERE student_id = ?";
                break;

            case 3:
                while (true) {
                    System.out.print("Enter new phone: ");
                    value1 = sc.nextLine().trim();

                    if (!value1.matches("[0-9]{10}")) {
                        System.out.println(
                                "Phone number must contain exactly 10 digits.");
                    } else {
                        break;
                    }
                }

                sql = "UPDATE students SET phone = ? WHERE student_id = ?";
                break;

            case 4:
                while (true) {
                    System.out.print("Enter new name: ");
                    value1 = sc.nextLine().trim();

                    if (value1.isEmpty()) {
                        System.out.println("Name cannot be empty.");
                    } else if (!value1.matches("[\\p{L} .'-]+")) {
                        System.out.println("Enter a valid student name.");
                    } else {
                        break;
                    }
                }

                while (true) {
                    System.out.print("Enter new email: ");
                    value2 = sc.nextLine().trim();

                    if (!value2.matches(
                            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                        System.out.println("Enter a valid email address.");
                    } else {
                        break;
                    }
                }

                while (true) {
                    System.out.print("Enter new phone: ");
                    value3 = sc.nextLine().trim();

                    if (!value3.matches("[0-9]{10}")) {
                        System.out.println(
                                "Phone number must contain exactly 10 digits.");
                    } else {
                        break;
                    }
                }

                sql = "UPDATE students SET name = ?, email = ?, phone = ? "
                        + "WHERE student_id = ?";
                break;

            default:
                return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (choice == 4) {
                ps.setString(1, value1);
                ps.setString(2, value2);
                ps.setString(3, value3);
                ps.setInt(4, id);
            } else {
                ps.setString(1, value1);
                ps.setInt(2, id);
            }

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                System.out.println(
                        "Update failed: Email already exists "
                                + "or a database constraint was violated.");
            } else {
                System.out.println(
                        "Database error during update: " + e.getMessage());
            }
        }
    }

    // Delete student
    public void deleteStudent(Scanner sc) {

        int id;

        while (true) {
            System.out.print("Enter student ID to delete: ");
            String input = sc.nextLine().trim();

            try {
                id = Integer.parseInt(input);

                if (id <= 0) {
                    System.out.println(
                            "Error: Student ID must be greater than zero.");
                } else {
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: Enter a valid numeric student ID.");
            }
        }

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student ID not found.");
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                System.out.println(
                        "Cannot delete this student because related records "
                                + "exist in other tables.");
            } else {
                System.out.println(
                        "Database error during deletion: " + e.getMessage());
            }
        }
    }
}