package com.fsre.attendance_tracker_backend.repo;

import com.fsre.attendance_tracker_backend.model.CompanyPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyPersonRepo extends JpaRepository<CompanyPerson, Long> {
    Optional<CompanyPerson> findByPersonIdAndCompanyId(Long personId, Long companyId);

    List<CompanyPerson> findByPersonId(Long personId);
}
