
package com.leave.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.leave.dto.EmployeeLeaveRequest;
import com.leave.dto.EmployeeLeaveResponse;
import com.leave.service.EmployeeLeaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/leaves")
@RequiredArgsConstructor
public class EmployeeLeaveController {

    private final EmployeeLeaveService employeeLeaveService;

    // Apply for Leave
    @PostMapping
    public ResponseEntity<EmployeeLeaveResponse> applyForLeave(
            @Valid @RequestBody EmployeeLeaveRequest request) {

        return new ResponseEntity<>(
                employeeLeaveService.applyForLeave(request),
                HttpStatus.CREATED);
    }

    // Get All Leave Applications
    @GetMapping
    public ResponseEntity<List<EmployeeLeaveResponse>> getAllLeaveApplications() {

        return ResponseEntity.ok(
                employeeLeaveService.getAllLeaveApplications());
    }

    // Get Leave Application By ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeLeaveResponse> getLeaveById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeLeaveService.getLeaveById(id));
    }

    // Update Leave Application
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeLeaveResponse> updateLeave(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeLeaveRequest request) {

        return ResponseEntity.ok(
                employeeLeaveService.updateLeave(id, request));
    }

    // Delete Leave Application
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeave(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeLeaveService.deleteLeave(id));
    }
}
