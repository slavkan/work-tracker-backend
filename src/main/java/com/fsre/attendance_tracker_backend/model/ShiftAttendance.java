package com.fsre.attendance_tracker_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShiftAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column
    private LocalDateTime departureTime;

    @ManyToOne
    @JoinColumn(name = "work_shift_id", nullable = false)
    private WorkShift workShift;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Person person;
}
