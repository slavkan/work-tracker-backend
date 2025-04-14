package com.fsre.attendance_tracker_backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PersonFilterDto {
    private String firstName;
    private String lastName;
    private String role;
    private Long companyId;
}
