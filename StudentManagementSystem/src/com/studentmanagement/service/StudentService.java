
package com.studentmanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.studentmanagement.model.Student;
import com.studentmanagement.exception.DuplicateStudentException;
import com.studentmanagement.exception.InvalidStudentDataException;
import com.studentmanagement.exception.StudentNotFoundException;

public class StudentService {

    private Map<Integer, Student> students;

    public StudentService() {
        students = new HashMap<>();
    }

    // 1. Add Student
    public void addStudent(Student student)
            throws DuplicateStudentException, InvalidStudentDataException {

        if (student == null) {
            throw new InvalidStudentDataException("Student cannot be null");
        }

        if (student.getId() <= 0) {
            throw new InvalidStudentDataException("Invalid student ID");
        }

        if (students.containsKey(student.getId())) {
            throw new DuplicateStudentException("Student ID already exists");
        }

        if (student.getName() == null ||
                !student.getName().trim().matches("[a-zA-Z ]+")) {
            throw new InvalidStudentDataException(
                    "Name must contain only letters and spaces");
        }

        if (student.getEmail() == null ||
                !student.getEmail().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidStudentDataException(
                    "Enter a valid email address");
        }

        if (student.getCourse() == null || student.getStatus() == null) {
            throw new InvalidStudentDataException(
                    "Course and status are required");
        }

        if (student.getMarks() < 0 || student.getMarks() > 100
                || !Double.isFinite(student.getMarks())) {
            throw new InvalidStudentDataException(
                    "Marks must be between 0 and 100");
        }

        if (student.getAttendance() < 0 || student.getAttendance() > 100) {
            throw new InvalidStudentDataException(
                    "Attendance must be between 0 and 100");
        }

        students.put(student.getId(), student);

        System.out.println("Student added successfully");
    }

    // 2. Display All Students
    public void displayAllStudents() {

        if (students.isEmpty()) {
            System.out.println("No students available");
            return;
        }

        for (Student student : students.values()) {
            System.out.println(student);
        }
    }

    // 3. Search Student by ID
    public Student searchById(int id)
            throws StudentNotFoundException {

        Student student = students.get(id);

        if (student == null) {
            throw new StudentNotFoundException("Student not found");
        }

        return student;
    }

    // 4. Search Student by Name
    public List<Student> searchByName(String name) {

        List<Student> result = new ArrayList<>();

        if (name == null || name.isBlank()) {
            return result;
        }

        for (Student student : students.values()) {
            if (student.getName().equalsIgnoreCase(name.trim())) {
                result.add(student);
            }
        }

        return result;
    }

    // 5. Update Student Marks
    public void updateMarks(int id, double marks)
            throws StudentNotFoundException, InvalidStudentDataException {

        if (marks < 0 || marks > 100 || !Double.isFinite(marks)) {
            throw new InvalidStudentDataException(
                    "Marks must be between 0 and 100");
        }

        Student student = searchById(id);
        student.setMarks(marks);

        System.out.println("Marks updated successfully");
    }

    // 6. Update Attendance
    public void updateAttendance(int id, int attendance)
            throws StudentNotFoundException, InvalidStudentDataException {

        if (attendance < 0 || attendance > 100) {
            throw new InvalidStudentDataException(
                    "Attendance must be between 0 and 100");
        }

        Student student = searchById(id);
        student.setAttendance(attendance);

        System.out.println("Attendance updated successfully");
    }

    // 7. Calculate Grade
    public String calculateGrade(int id)
            throws StudentNotFoundException {

        Student student = searchById(id);
        double marks = student.getMarks();

        if (marks >= 90) {
            return "A";
        } else if (marks >= 80) {
            return "B";
        } else if (marks >= 70) {
            return "C";
        } else if (marks >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    // 8. Remove Student
    public void removeStudent(int id)
            throws StudentNotFoundException {

        if (!students.containsKey(id)) {
            throw new StudentNotFoundException("Student not found");
        }

        students.remove(id);

        System.out.println("Student removed successfully");
    }

    // 9. Display Topper
    public List<Student> displayTopper() {

        List<Student> toppers = new ArrayList<>();

        if (students.isEmpty()) {
            System.out.println("No students available");
            return toppers;
        }

        double highestMarks = -1;

        for (Student student : students.values()) {
            if (student.getMarks() > highestMarks) {
                highestMarks = student.getMarks();
            }
        }

        for (Student student : students.values()) {
            if (student.getMarks() == highestMarks) {
                toppers.add(student);
            }
        }

        System.out.println("Topper(s):");

        for (Student student : toppers) {
            System.out.println(student);
        }

        return toppers;
    }

    // 10. Get All Students
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
}

