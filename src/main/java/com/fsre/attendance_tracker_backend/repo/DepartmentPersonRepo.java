package com.fsre.attendance_tracker_backend.repo;

import com.fsre.attendance_tracker_backend.model.DepartmentPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentPersonRepo extends JpaRepository<DepartmentPerson, Long> {

    Optional<DepartmentPerson> findByPersonIdAndDepartmentId(Long personId, Long departmentId);

    List<DepartmentPerson> findByPersonId(Long personId);

}
