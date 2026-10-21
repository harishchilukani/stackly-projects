
package studentmanagement;

import java.util.Scanner;

public class StudentManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentService service = new StudentService();
        CourseService courseService = new CourseService();
        EnrollmentService enrollmentService = new EnrollmentService();
        AttendanceService attendanceService = new AttendanceService();
        MarksService marksService = new MarksService();
        ResultService resultService = new ResultService();
        EnrollmentTransactionService transactionService =
                new EnrollmentTransactionService();

        int choice;

        do {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Register Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Add Course");
            System.out.println("6. View All Courses");
            System.out.println("7. Enroll Student");
            System.out.println("8. Mark Attendance");
            System.out.println("9. View Attendance");
            System.out.println("10. Enter Marks");
            System.out.println("11. View Marks");
            System.out.println("12. Generate Result");
            System.out.println("13. Register Student and Enroll (Transaction)");
            System.out.println("14. Exit");

            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    service.registerStudent(sc);
                    break;

                case 2:
                    service.viewAllStudents();
                    break;

                case 3:
                    service.updateStudent(sc);
                    break;

                case 4:
                    service.deleteStudent(sc);
                    break;

                case 5:
                    courseService.addCourse(sc);
                    break;

                case 6:
                    courseService.viewAllCourses();
                    break;

                case 7:
                    enrollmentService.enrollStudent(sc);
                    break;

                case 8:
                    attendanceService.markAttendance(sc);
                    break;

                case 9:
                    attendanceService.viewAttendance();
                    break;

                case 10:
                    marksService.enterMarks(sc);
                    break;

                case 11:
                    marksService.viewMarks();
                    break;

                case 12:
                    resultService.generateResult(sc);
                    break;

                case 13:
                    transactionService.registerAndEnrollStudent(sc);
                    break;

                case 14:
                    System.out.println(
                            "Exiting Student Management System..."
                    );
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }

        } while (choice != 14);

        sc.close();
    }
}
