
package com.leave.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.leave.dto.EmployeeLeaveRequest;
import com.leave.dto.EmployeeLeaveResponse;
import com.leave.exception.LeaveNotFoundException;
import com.leave.model.EmployeeLeave;

@Service
public class EmployeeLeaveService {

    private final List<EmployeeLeave> leaveApplications = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    // Apply for Leave
    public EmployeeLeaveResponse applyForLeave(EmployeeLeaveRequest request) {

        Long employeeId = idGenerator.getAndIncrement();

        EmployeeLeave leave = new EmployeeLeave(
                employeeId,
                request.getEmployeeName(),
                request.getLeaveType(),
                request.getNumberOfDays(),
                request.getReason()
        );

        leaveApplications.add(leave);

        return convertToResponse(leave);
    }

    // Get All Leave Applications
    public List<EmployeeLeaveResponse> getAllLeaveApplications() {

        return leaveApplications.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get Leave Application By ID
    public EmployeeLeaveResponse getLeaveById(Long employeeId) {

        EmployeeLeave leave = findLeaveById(employeeId);

        return convertToResponse(leave);
    }

    // Update Leave Application
    public EmployeeLeaveResponse updateLeave(
            Long employeeId, EmployeeLeaveRequest request) {

        EmployeeLeave leave = findLeaveById(employeeId);

        leave.setEmployeeName(request.getEmployeeName());
        leave.setLeaveType(request.getLeaveType());
        leave.setNumberOfDays(request.getNumberOfDays());
        leave.setReason(request.getReason());

        return convertToResponse(leave);
    }

    // Delete Leave Application
    public String deleteLeave(Long employeeId) {

        EmployeeLeave leave = findLeaveById(employeeId);

        leaveApplications.remove(leave);

        return "Leave application deleted successfully with ID: " + employeeId;
    }

    // Find Leave Application By ID
    private EmployeeLeave findLeaveById(Long employeeId) {

        return leaveApplications.stream()
                .filter(leave -> leave.getEmployeeId().equals(employeeId))
                .findFirst()
                .orElseThrow(() ->
                        new LeaveNotFoundException(
                                "Leave application not found with ID: " + employeeId));
    }

    // Convert Model To Response DTO
    private EmployeeLeaveResponse convertToResponse(EmployeeLeave leave) {

        return new EmployeeLeaveResponse(
                leave.getEmployeeId(),
                leave.getEmployeeName(),
                leave.getLeaveType(),
                leave.getNumberOfDays(),
                leave.getReason()
        );
    }
}
