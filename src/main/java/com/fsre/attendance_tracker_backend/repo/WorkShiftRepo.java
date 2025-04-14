package com.fsre.attendance_tracker_backend.repo;

import com.fsre.attendance_tracker_backend.model.WorkShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkShiftRepo extends JpaRepository<WorkShift, Long> {

    List<WorkShift> findByDepartmentId(Long departmentId);
}
