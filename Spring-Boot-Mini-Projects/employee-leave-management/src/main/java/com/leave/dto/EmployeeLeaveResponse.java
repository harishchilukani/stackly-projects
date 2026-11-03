
package com.leave.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLeaveResponse {

    private Long employeeId;
    private String employeeName;
    private String leaveType;
    private int numberOfDays;
    private String reason;
}
