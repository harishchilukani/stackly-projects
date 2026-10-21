
package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MarksService {

    // Enter marks for a student
    public void enterMarks(Scanner sc) {

        int studentId;

        while (true) {
            System.out.print("Enter student ID: ");

            try {
                studentId = Integer.parseInt(sc.nextLine().trim());

                if (studentId <= 0) {
                    System.out.println("Student ID must be positive.");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid numeric student ID.");
            }
        }

        int courseId;

        while (true) {
            System.out.print("Enter course ID: ");

            try {
                courseId = Integer.parseInt(sc.nextLine().trim());

                if (courseId <= 0) {
                    System.out.println("Course ID must be positive.");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid numeric course ID.");
            }
        }

        double marks;

        while (true) {
            System.out.print("Enter marks obtained (0-100): ");

            try {
                String input = sc.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Marks cannot be empty.");
                    continue;
                }

                marks = Double.parseDouble(input);

                if (!Double.isFinite(marks) || marks < 0 || marks > 100) {
                    System.out.println(
                            "Invalid marks! Enter a number between 0 and 100.");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter valid numeric marks.");
            }
        }

        String studentSql =
                "SELECT student_id FROM students WHERE student_id = ?";

        String courseSql =
                "SELECT course_id FROM courses WHERE course_id = ?";

        String enrollmentSql =
                "SELECT 1 FROM enrollments WHERE student_id = ? AND course_id = ?";

        String marksSql =
                "INSERT INTO marks (student_id, course_id, marks_obtained) "
                + "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            // Check whether the student exists
            try (PreparedStatement ps =
                    con.prepareStatement(studentSql)) {

                ps.setInt(1, studentId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Student ID does not exist.");
                        return;
                    }
                }
            }

            // Check whether the course exists
            try (PreparedStatement ps =
                    con.prepareStatement(courseSql)) {

                ps.setInt(1, courseId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Course ID does not exist.");
                        return;
                    }
                }
            }

            // Check whether the student is enrolled in the course
            try (PreparedStatement ps =
                    con.prepareStatement(enrollmentSql)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println(
                                "Student is not enrolled in this course.");
                        return;
                    }
                }
            }

            // Insert marks
            try (PreparedStatement ps =
                    con.prepareStatement(marksSql)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setDouble(3, marks);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Marks entered successfully!");
                } else {
                    System.out.println("Marks could not be entered.");
                }
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                System.out.println(
                        "Marks could not be entered because of a "
                        + "duplicate record or database constraint.");
            } else {
                System.out.println(
                        "Database error while entering marks: "
                        + e.getMessage());
            }
        }
    }

    // View marks for all students
    public void viewMarks() {

        String sql = "SELECT s.student_id, s.name, "
                   + "c.course_name, m.marks_obtained "
                   + "FROM marks m "
                   + "JOIN students s ON m.student_id = s.student_id "
                   + "JOIN courses c ON m.course_id = c.course_id "
                   + "ORDER BY s.student_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n===== STUDENT MARKS =====");

            while (rs.next()) {
                found = true;

                System.out.println(
                    "Student ID: " + rs.getInt("student_id")
                    + " | Name: " + rs.getString("name")
                    + " | Course: " + rs.getString("course_name")
                    + " | Marks: " + rs.getDouble("marks_obtained")
                );
            }

            if (!found) {
                System.out.println("No marks records found.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error fetching marks: " + e.getMessage());
        }
    }
}
