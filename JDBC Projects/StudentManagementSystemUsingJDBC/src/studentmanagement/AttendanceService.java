package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class AttendanceService {

    // Mark student attendance
    public void markAttendance(Scanner sc) {

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

        String status;

        while (true) {
            System.out.println("1. Present");
            System.out.println("2. Absent");
            System.out.print("Enter attendance status: ");

            String input = sc.nextLine().trim();

            if (input.equals("1")) {
                status = "Present";
                break;
            } else if (input.equals("2")) {
                status = "Absent";
                break;
            } else {
                System.out.println("Invalid choice. Enter 1 or 2.");
            }
        }

        String studentSql =
                "SELECT student_id FROM students WHERE student_id = ?";
        String courseSql =
                "SELECT course_id FROM courses WHERE course_id = ?";
        String enrollmentSql =
                "SELECT 1 FROM enrollments WHERE student_id = ? AND course_id = ?";
        String insertSql =
                "INSERT INTO attendance "
                + "(student_id, course_id, attendance_date, status) "
                + "VALUES (?, ?, ?, ?)";

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

            // Check whether the student is enrolled in this course
            try (PreparedStatement ps =
                    con.prepareStatement(enrollmentSql)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println(
                                "This student is not enrolled in this course.");
                        return;
                    }
                }
            }

            // Insert attendance
            try (PreparedStatement ps =
                    con.prepareStatement(insertSql)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setDate(3,
                        java.sql.Date.valueOf(LocalDate.now()));
                ps.setString(4, status);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Attendance marked successfully!");
                } else {
                    System.out.println(
                            "Attendance could not be marked.");
                }
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                System.out.println(
                        "Attendance could not be marked because of a "
                        + "duplicate record or database constraint.");
            } else {
                System.out.println(
                        "Database error while marking attendance: "
                        + e.getMessage());
            }
        }
    }

    // View all attendance records
    public void viewAttendance() {

        String sql = """
            SELECT s.student_id, s.name, c.course_name,
                   a.attendance_date, a.status
            FROM attendance a
            JOIN students s
                ON a.student_id = s.student_id
            JOIN courses c
                ON a.course_id = c.course_id
            ORDER BY a.attendance_date DESC
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            System.out.println("\n===== ATTENDANCE RECORDS =====");

            while (rs.next()) {
                found = true;

                System.out.println(
                    "Student ID: " + rs.getInt("student_id")
                    + " | Name: " + rs.getString("name")
                    + " | Course: " + rs.getString("course_name")
                    + " | Date: " + rs.getDate("attendance_date")
                    + " | Status: " + rs.getString("status")
                );
            }

            if (!found) {
                System.out.println("No attendance records found.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error fetching attendance: " + e.getMessage());
        }
    }
}
