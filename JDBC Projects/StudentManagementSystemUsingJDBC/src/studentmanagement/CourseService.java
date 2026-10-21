package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CourseService {

    // Add course
    public void addCourse(Scanner sc) {

        String courseName;

        while (true) {
            System.out.print("Enter course name: ");
            courseName = sc.nextLine().trim();

            if (courseName.isEmpty()) {
                System.out.println("Error: Course name cannot be empty.");
            } else if (!courseName.matches("[\\p{L}0-9 &().'-]+")) {
                System.out.println(
                        "Error: Course name contains invalid characters.");
            } else {
                break;
            }
        }

        int duration;

        while (true) {
            System.out.print("Enter duration in months: ");
            String input = sc.nextLine().trim();

            try {
                duration = Integer.parseInt(input);

                if (duration <= 0) {
                    System.out.println(
                            "Error: Duration must be greater than zero.");
                } else if (duration > 120) {
                    System.out.println(
                            "Error: Duration cannot exceed 120 months.");
                } else {
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: Enter a valid whole number for duration.");
            }
        }

        String sql =
                "INSERT INTO courses (course_name, duration_months) "
                        + "VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseName);
            ps.setInt(2, duration);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Course added successfully!");
            } else {
                System.out.println("Course could not be added.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error adding course: " + e.getMessage());
        }
    }

    // View all courses
    public void viewAllCourses() {

        String sql = "SELECT * FROM courses ORDER BY course_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n===== AVAILABLE COURSES =====");

            while (rs.next()) {
                found = true;

                System.out.println(
                        "Course ID: " + rs.getInt("course_id")
                        + " | Name: " + rs.getString("course_name")
                        + " | Duration: "
                        + rs.getInt("duration_months")
                        + " months");
            }

            if (!found) {
                System.out.println("No courses available.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error fetching courses: " + e.getMessage());
        }
    }
}