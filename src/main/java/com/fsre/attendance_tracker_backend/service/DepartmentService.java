package com.fsre.attendance_tracker_backend.service;

import com.fsre.attendance_tracker_backend.model.Company;
import com.fsre.attendance_tracker_backend.model.Department;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.repo.CompanyRepo;
import com.fsre.attendance_tracker_backend.repo.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepo subjectRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private CompanyRepo companyRepo;

    public List<Department> getAllDepartmentsByCompany(Long companyId) {
        return Optional.ofNullable(subjectRepo.findByCompanyId(companyId)).orElse(Collections.emptyList());
    }

    public Department addDepartment(Department department) {
        Long companyId = department.getCompany().getId();
        Company company1 = companyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));

        return subjectRepo.save(department);
    }

    public Department updateDepartment(Long id, Department department) {
        Optional<Department> existingSubjectOptional = departmentRepo.findById(id);
        if(existingSubjectOptional.isPresent()) {
            Department existingSubject = existingSubjectOptional.get();

            existingSubject.setName(department.getName());
            existingSubject.setCompany(department.getCompany());

            return subjectRepo.save(existingSubject);
        } else {
            throw new RuntimeException("Department not found with id " + id);
        }
    }

    public ApiResponse deleteDepartment(Long id) {
        Optional<Department> existingDepartmentOptional = departmentRepo.findById(id);
        if (existingDepartmentOptional.isPresent()) {
            try {
                subjectRepo.deleteById(id);
                return new ApiResponse("Department deleted successfully");
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Cannot delete department due to foreign key constraints");
            }
        } else {
            throw new RuntimeException("Department not found with id " + id);
        }
    }

}
