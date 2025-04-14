package com.fsre.attendance_tracker_backend.service;

import com.fsre.attendance_tracker_backend.model.Person;
import com.fsre.attendance_tracker_backend.model.Department;
import com.fsre.attendance_tracker_backend.model.DepartmentPerson;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.repo.PersonRepo;
import com.fsre.attendance_tracker_backend.repo.DepartmentPersonRepo;
import com.fsre.attendance_tracker_backend.repo.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentPersonService {

    @Autowired
    private DepartmentPersonRepo departmentPersonRepo;

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    public DepartmentPerson getDepartmentPerson (Long id) {
        return departmentPersonRepo.findById(id).orElse(null);
    }

    public DepartmentPerson assignPersonToDepartment(Long personId, Long departmentId) {
        Person person = personRepo.findById(personId).orElseThrow(() -> new RuntimeException("Person not found"));
        Department department = departmentRepo.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found"));
        if (departmentPersonRepo.findByPersonIdAndDepartmentId(personId, departmentId).isPresent()) {
            throw new RuntimeException("Person and department are already connected");
        }

        DepartmentPerson departmentPerson = new DepartmentPerson();
        departmentPerson.setPerson(person);
        departmentPerson.setDepartment(department);

        return departmentPersonRepo.save(departmentPerson);
    }

    public List<DepartmentPerson> getAllDepartmentsByPerson(Long personId) {
        return departmentPersonRepo.findByPersonId(personId);
    }

    public ApiResponse removePersonFromDepartment(Long personId, Long departmentId) {
        Optional<DepartmentPerson> existingDepartmentPersonOptional = departmentPersonRepo.findByPersonIdAndDepartmentId(personId, departmentId);
        if (existingDepartmentPersonOptional.isPresent()) {
            try {
                departmentPersonRepo.delete(existingDepartmentPersonOptional.get());
                return new ApiResponse("User removed from department");
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Cannot remove user from department due to foreign key constraints");
            }
        } else {
            throw new RuntimeException("User and department connection not found");
        }
    }

}
