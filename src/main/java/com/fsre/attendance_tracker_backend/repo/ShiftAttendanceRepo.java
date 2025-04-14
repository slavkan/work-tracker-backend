package com.fsre.attendance_tracker_backend.repo;

import com.fsre.attendance_tracker_backend.model.ShiftAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShiftAttendanceRepo extends JpaRepository<ShiftAttendance, Long> {

    List<ShiftAttendance> findByWorkShiftId(Long workShiftId);

    Optional<ShiftAttendance> findByWorkShiftIdAndPersonId(Long workShiftId, Long personId);

}
