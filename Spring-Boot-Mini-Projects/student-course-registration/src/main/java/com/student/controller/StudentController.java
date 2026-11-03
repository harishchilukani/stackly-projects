package com.student.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.dto.StudentRequest;
import com.student.dto.StudentResponse;
import com.student.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Register student
    @PostMapping
    public ResponseEntity<StudentResponse> registerStudent(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.registerStudent(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // View all students
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // Find student by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // Update student
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        return ResponseEntity.ok(
                studentService.updateStudent(id, request));
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable Long id) {

        return ResponseEntity.ok(studentService.deleteStudent(id));
    }

    // Search by student name
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchByName(
            @RequestParam String name) {

        return ResponseEntity.ok(studentService.searchByName(name));
    }

    // Search by course
    @GetMapping("/course")
    public ResponseEntity<List<StudentResponse>> searchByCourse(
            @RequestParam String name) {

        return ResponseEntity.ok(studentService.searchByCourse(name));
    }

    // Search by email
    @GetMapping("/email")
    public ResponseEntity<StudentResponse> searchByEmail(
            @RequestParam String email) {

        return ResponseEntity.ok(studentService.searchByEmail(email));
    }

    // Filter by age range
    @GetMapping("/age")
    public ResponseEntity<List<StudentResponse>> filterByAge(
            @RequestParam int min,
            @RequestParam int max) {

        return ResponseEntity.ok(studentService.filterByAge(min, max));
    }

    // Sort students
    @GetMapping("/sort")
    public ResponseEntity<List<StudentResponse>> sortStudents(
            @RequestParam String by,
            @RequestParam String order) {

        return ResponseEntity.ok(
                studentService.sortStudents(by, order));
    }

    // Course-wise student count
    @GetMapping("/course-count")
    public ResponseEntity<Map<String, Long>> getCourseWiseCount() {

        return ResponseEntity.ok(studentService.getCourseWiseCount());
    }
}