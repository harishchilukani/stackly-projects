package com.student.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.student.dto.StudentRequest;
import com.student.dto.StudentResponse;
import com.student.exception.DuplicateStudentException;
import com.student.exception.StudentNotFoundException;
import com.student.model.Student;

@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    // Register a new student
    public StudentResponse registerStudent(StudentRequest request) {

        checkDuplicateEmail(request.getEmail(), null);

        Student student = new Student(
                idGenerator.getAndIncrement(),
                request.getStudentName(),
                request.getCourseName(),
                request.getEmail(),
                request.getAge()
        );

        students.add(student);

        return convertToResponse(student);
    }

    // View all students
    public List<StudentResponse> getAllStudents() {
        return students.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Find student by ID
    public StudentResponse getStudentById(Long id) {
        return convertToResponse(findStudentById(id));
    }

    // Update student details
    public StudentResponse updateStudent(Long id, StudentRequest request) {

        Student student = findStudentById(id);

        checkDuplicateEmail(request.getEmail(), id);

        student.setStudentName(request.getStudentName());
        student.setCourseName(request.getCourseName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());

        return convertToResponse(student);
    }

    // Delete student registration
    public String deleteStudent(Long id) {

        Student student = findStudentById(id);
        students.remove(student);

        return "Student registration deleted successfully";
    }

    // Search students by name
    public List<StudentResponse> searchByName(String name) {

        return students.stream()
                .filter(student -> student.getStudentName()
                        .toLowerCase()
                        .contains(name.toLowerCase()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Search students by course
    public List<StudentResponse> searchByCourse(String courseName) {

        return students.stream()
                .filter(student -> student.getCourseName()
                        .toLowerCase()
                        .contains(courseName.toLowerCase()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Search student by email
    public StudentResponse searchByEmail(String email) {

        return students.stream()
                .filter(student -> student.getEmail()
                        .equalsIgnoreCase(email))
                .findFirst()
                .map(this::convertToResponse)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with email: " + email));
    }

    // Filter students by age range
    public List<StudentResponse> filterByAge(int min, int max) {

        if (min < 18) {
            throw new IllegalArgumentException(
                    "Minimum age must be at least 18");
        }

        if (max < min) {
            throw new IllegalArgumentException(
                    "Maximum age must be greater than or equal to minimum age");
        }

        return students.stream()
                .filter(student -> student.getAge() >= min
                        && student.getAge() <= max)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Sort students by name or age
    public List<StudentResponse> sortStudents(String by, String order) {

        Comparator<Student> comparator;

        if ("name".equalsIgnoreCase(by)) {
            comparator = Comparator.comparing(
                    Student::getStudentName,
                    String.CASE_INSENSITIVE_ORDER
            );
        } else if ("age".equalsIgnoreCase(by)) {
            comparator = Comparator.comparingInt(Student::getAge);
        } else {
            throw new IllegalArgumentException(
                    "Sort field must be name or age");
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        } else if (!"asc".equalsIgnoreCase(order)) {
            throw new IllegalArgumentException(
                    "Sort order must be asc or desc");
        }

        return students.stream()
                .sorted(comparator)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get student count for each course
    public Map<String, Long> getCourseWiseCount() {

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::getCourseName,
                        Collectors.counting()
                ));
    }

    // Check duplicate email
    private void checkDuplicateEmail(String email, Long excludeId) {

        boolean exists = students.stream()
                .anyMatch(student ->
                        student.getEmail().equalsIgnoreCase(email)
                                && !student.getStudentId().equals(excludeId));

        if (exists) {
            throw new DuplicateStudentException(
                    "Email is already registered: " + email);
        }
    }

    // Find student by ID
    private Student findStudentById(Long id) {

        return students.stream()
                .filter(student -> student.getStudentId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with ID: " + id));
    }

    // Convert Student entity to response DTO
    private StudentResponse convertToResponse(Student student) {

        return new StudentResponse(
                student.getStudentId(),
                student.getStudentName(),
                student.getCourseName(),
                student.getEmail(),
                student.getAge()
        );
    }
}