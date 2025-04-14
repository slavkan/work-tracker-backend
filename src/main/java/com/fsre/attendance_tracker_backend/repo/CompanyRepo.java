package com.fsre.attendance_tracker_backend.repo;

import com.fsre.attendance_tracker_backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepo extends JpaRepository<Company, Long> {
}
