package com.fsre.attendance_tracker_backend.controller;

import com.fsre.attendance_tracker_backend.model.DepartmentPerson;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.service.DepartmentPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department-person")
@CrossOrigin
public class DepartmentPersonController {

    @Autowired
    DepartmentPersonService departmentPersonService;

    public DepartmentPersonController(DepartmentPersonService departmentPersonService) {
        this.departmentPersonService = departmentPersonService;
    }

    @PostMapping("")
    public ResponseEntity<?> assignPersonToDepartment(@RequestParam Long personId, @RequestParam Long departmentId) {
        try {
            DepartmentPerson departmentPerson = departmentPersonService.assignPersonToDepartment(personId, departmentId);
            return new ResponseEntity<>(departmentPerson, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /* Get all departments person is connected to */
    @GetMapping("")
    public ResponseEntity<?> getPersonDepartments(@RequestParam Long personId) {
        try {
            List<DepartmentPerson> departments = departmentPersonService.getAllDepartmentsByPerson(personId);
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> removePersonFromDepartment(@RequestParam Long personId, @RequestParam Long departmentId) {
        try {
            ApiResponse response = departmentPersonService.removePersonFromDepartment(personId, departmentId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("foreign key constraints")) {
                return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
            }
        }
    }

}
