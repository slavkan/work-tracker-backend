package com.fsre.attendance_tracker_backend.repo;

import com.fsre.attendance_tracker_backend.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepo extends JpaRepository<Department, Long> {

    List<Department> findByCompanyId(Long companyId);
}
