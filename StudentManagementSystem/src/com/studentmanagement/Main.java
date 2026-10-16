
package com.studentmanagement;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.studentmanagement.model.Course;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.StudentStatus;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.service.StudentFileService;
import com.studentmanagement.exception.DuplicateStudentException;
import com.studentmanagement.exception.InvalidStudentDataException;
import com.studentmanagement.exception.StudentNotFoundException;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentService studentService = new StudentService();
        StudentFileService fileService = new StudentFileService();

        // Load saved students when application starts
        try {
            List<Student> loadedStudents = fileService.loadStudents();

            for (Student student : loadedStudents) {
                studentService.addStudent(student);
            }

            System.out.println("Student data initialized");
        } catch (IOException | DuplicateStudentException |
                 InvalidStudentDataException e) {
            System.out.println("Loading error: " + e.getMessage());
        }

        boolean running = true;

        while (running) {

            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Search Student by Name");
            System.out.println("5. Update Marks");
            System.out.println("6. Update Attendance");
            System.out.println("7. Calculate Grade");
            System.out.println("8. Remove Student");
            System.out.println("9. Display Topper");
            System.out.println("10. Save Students");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        System.out.print("Enter student ID: ");
                        int id = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter email: ");
                        String email = sc.nextLine();

                        System.out.print("Enter marks: ");
                        double marks = Double.parseDouble(sc.nextLine());

                        System.out.print("Enter attendance: ");
                        int attendance = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter status (ACTIVE, INACTIVE, COMPLETED): ");
                        StudentStatus status = StudentStatus.valueOf(
                                sc.nextLine().trim().toUpperCase());

                        System.out.print("Enter course ID: ");
                        int courseId = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter course name: ");
                        String courseName = sc.nextLine();

                        System.out.print("Enter course duration (months): ");
                        int duration = Integer.parseInt(sc.nextLine());

                        Course course = new Course(
                                courseId, courseName, duration);

                        Student student = new Student(
                                id, name, email, marks,
                                attendance, status, course);

                        studentService.addStudent(student);
                        break;

                    case 2:
                        studentService.displayAllStudents();
                        break;

                    case 3:
                        System.out.print("Enter student ID: ");
                        id = Integer.parseInt(sc.nextLine());

                        System.out.println(
                                studentService.searchById(id));
                        break;

                    case 4:
                        System.out.print("Enter student name: ");
                        name = sc.nextLine();

                        List<Student> results =
                                studentService.searchByName(name);

                        if (results.isEmpty()) {
                            System.out.println("No students found");
                        } else {
                            for (Student s : results) {
                                System.out.println(s);
                            }
                        }
                        break;

                    case 5:
                        System.out.print("Enter student ID: ");
                        id = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter new marks: ");
                        marks = Double.parseDouble(sc.nextLine());

                        studentService.updateMarks(id, marks);
                        break;

                    case 6:
                        System.out.print("Enter student ID: ");
                        id = Integer.parseInt(sc.nextLine());

                        System.out.print("Enter new attendance: ");
                        attendance = Integer.parseInt(sc.nextLine());

                        studentService.updateAttendance(id, attendance);
                        break;

                    case 7:
                        System.out.print("Enter student ID: ");
                        id = Integer.parseInt(sc.nextLine());

                        System.out.println("Grade: " +
                                studentService.calculateGrade(id));
                        break;

                    case 8:
                        System.out.print("Enter student ID: ");
                        id = Integer.parseInt(sc.nextLine());

                        studentService.removeStudent(id);
                        break;

                    case 9:
                        studentService.displayTopper();
                        break;

                    case 10:
                        fileService.saveStudents(
                                studentService.getAllStudents());
                        break;

                    case 11:
                        fileService.saveStudents(
                                studentService.getAllStudents());
                        running = false;
                        System.out.println("Exiting application...");
                        break;

                    default:
                        System.out.println("Invalid choice");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            } catch (DuplicateStudentException |
                     InvalidStudentDataException |
                     StudentNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }

        sc.close();
    }
}

