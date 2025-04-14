package com.fsre.attendance_tracker_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkShiftState state;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private String codeForArrival;

    @Column
    private String codeForArrivalPrevious;

    /* All times (arrival, departure, endTime) will be offset by X minutes
    Helps for testing application, not intended for production */
    @Column
    private Long offsetInMinutes;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "supervisor_id", nullable = false)
    private Person person;

    @OneToMany(mappedBy = "workShift", fetch = FetchType.LAZY)
    private Set<ShiftAttendance> shiftAttendances;

}
