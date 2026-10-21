
package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ResultService {

    private static final double PASS_MARKS = 35.0;

    public void generateResult(Scanner sc) {

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

        String sql = """
                SELECT s.student_id, s.name,
                       c.course_name, m.marks_obtained
                FROM marks m
                JOIN students s
                    ON m.student_id = s.student_id
                JOIN courses c
                    ON m.course_id = c.course_id
                WHERE s.student_id = ?
                ORDER BY c.course_name
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;
                boolean allPassed = true;
                double totalMarks = 0;
                int subjectCount = 0;
                String studentName = "";

                System.out.println("\n===== STUDENT RESULT =====");

                while (rs.next()) {
                    found = true;

                    studentName = rs.getString("name");
                    String courseName = rs.getString("course_name");
                    double marks = rs.getDouble("marks_obtained");

                    totalMarks += marks;
                    subjectCount++;

                    String result = marks >= PASS_MARKS
                            ? "PASS" : "FAIL";

                    if (marks < PASS_MARKS) {
                        allPassed = false;
                    }

                    System.out.println(
                            "Course: " + courseName
                            + " | Marks: " + marks
                            + " | Result: " + result
                    );
                }

                if (!found) {
                    System.out.println(
                            "No marks found for student ID: " + studentId);
                    return;
                }

                double percentage = totalMarks / subjectCount;

                System.out.println("--------------------------------");
                System.out.println("Student ID: " + studentId);
                System.out.println("Student Name: " + studentName);
                System.out.println(
                        "Total Marks: " + totalMarks
                        + " / " + (subjectCount * 100));

                System.out.printf("Percentage: %.2f%%%n", percentage);

                System.out.println(
                        "Final Result: " + (allPassed ? "PASS" : "FAIL"));
            }

        } catch (SQLException e) {
            System.out.println(
                    "Database error while generating result: "
                    + e.getMessage());
        }
    }
}
