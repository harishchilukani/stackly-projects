
package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class EnrollmentTransactionService {

    public void registerAndEnrollStudent(Scanner sc) {

        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        System.out.print("Enter student email: ");
        String email = sc.nextLine();

        System.out.print("Enter student phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter course ID: ");
        int courseId = Integer.parseInt(sc.nextLine());

        String studentSql =
                "INSERT INTO students (name, email, phone) VALUES (?, ?, ?)";

        String enrollmentSql =
                "INSERT INTO enrollments "
                + "(student_id, course_id, enrollment_date) "
                + "VALUES (?, ?, ?)";

        Connection con = null;

        try {
            con = DBConnection.getConnection();

            // Start transaction
            con.setAutoCommit(false);

            int studentId;

            // Step 1: Register student
            try (PreparedStatement studentPs =
                         con.prepareStatement(
                                 studentSql,
                                 PreparedStatement.RETURN_GENERATED_KEYS)) {

                studentPs.setString(1, name);
                studentPs.setString(2, email);
                studentPs.setString(3, phone);

                int rows = studentPs.executeUpdate();

                if (rows == 0) {
                    throw new SQLException("Student registration failed.");
                }

                try (var keys = studentPs.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException(
                                "Could not retrieve generated student ID."
                        );
                    }

                    studentId = keys.getInt(1);
                }
            }

            // Step 2: Enroll the newly registered student
            try (PreparedStatement enrollmentPs =
                         con.prepareStatement(enrollmentSql)) {

                enrollmentPs.setInt(1, studentId);
                enrollmentPs.setInt(2, courseId);
                enrollmentPs.setDate(
                        3,
                        java.sql.Date.valueOf(LocalDate.now())
                );

                int rows = enrollmentPs.executeUpdate();

                if (rows == 0) {
                    throw new SQLException("Student enrollment failed.");
                }
            }

            // Both operations succeeded
            con.commit();

            System.out.println(
                    "Student registered and enrolled successfully!"
            );
            System.out.println("Generated Student ID: " + studentId);

        } catch (SQLException e) {

            // Cancel all uncommitted operations
            if (con != null) {
                try {
                    con.rollback();
                    System.out.println(
                            "Transaction rolled back. No changes were saved."
                    );
                } catch (SQLException rollbackError) {
                    System.out.println(
                            "Rollback failed: " + rollbackError.getMessage()
                    );
                    e.addSuppressed(rollbackError);
                }
            }

            System.out.println("Transaction failed: " + e.getMessage());

        } finally {

            if (con != null) {
                try {
                    // Restore the default connection behavior
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println(
                            "Error closing connection: " + e.getMessage()
                    );
                }
            }
        }
    }
}
