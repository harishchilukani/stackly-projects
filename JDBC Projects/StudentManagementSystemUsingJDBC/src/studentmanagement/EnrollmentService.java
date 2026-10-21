package studentmanagement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class EnrollmentService {

    public void enrollStudent(Scanner sc) {

        int studentId;

        while (true) {
            System.out.print("Enter student ID: ");
            String input = sc.nextLine().trim();

            try {
                studentId = Integer.parseInt(input);

                if (studentId <= 0) {
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

        int courseId;

        while (true) {
            System.out.print("Enter course ID: ");
            String input = sc.nextLine().trim();

            try {
                courseId = Integer.parseInt(input);

                if (courseId <= 0) {
                    System.out.println(
                            "Error: Course ID must be greater than zero.");
                } else {
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: Enter a valid numeric course ID.");
            }
        }

        String studentSql =
                "SELECT student_id FROM students WHERE student_id = ?";

        String courseSql =
                "SELECT course_id FROM courses WHERE course_id = ?";

        String duplicateSql =
                "SELECT enrollment_id FROM enrollments "
                        + "WHERE student_id = ? AND course_id = ?";

        String insertSql =
                "INSERT INTO enrollments "
                        + "(student_id, course_id, enrollment_date) "
                        + "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            // Check whether the student exists
            try (PreparedStatement ps =
                         con.prepareStatement(studentSql)) {

                ps.setInt(1, studentId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println(
                                "Error: Student ID does not exist.");
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
                        System.out.println(
                                "Error: Course ID does not exist.");
                        return;
                    }
                }
            }

            // Check whether this student is already enrolled
            try (PreparedStatement ps =
                         con.prepareStatement(duplicateSql)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println(
                                "Error: Student is already enrolled in this course.");
                        return;
                    }
                }
            }

            // Enroll the student
            try (PreparedStatement ps =
                         con.prepareStatement(insertSql)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setDate(3,
                        java.sql.Date.valueOf(LocalDate.now()));

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                            "Student enrolled successfully!");
                } else {
                    System.out.println(
                            "Student enrollment failed.");
                }
            }

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                System.out.println(
                        "Enrollment failed because of a duplicate "
                                + "record or database constraint.");
            } else {
                System.out.println(
                        "Database error during enrollment: "
                                + e.getMessage());
            }
        }
    }
}