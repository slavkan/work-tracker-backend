package com.fsre.attendance_tracker_backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkerArrivalDto {

    private Long workShiftId;
    private String departmentName;
    private Long personId;
    private String code;
    private String firstName;
    private String lastName;
    private String arrivalTime;
    private String departureTime;
    private String message;
}
