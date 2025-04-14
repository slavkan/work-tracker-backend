package com.fsre.attendance_tracker_backend.controller;

import com.fsre.attendance_tracker_backend.model.CompanyPerson;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.service.CompanyPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company-person")
@CrossOrigin
public class CompanyPersonController {

    @Autowired
    private CompanyPersonService companyPersonService;

    public CompanyPersonController(CompanyPersonService companyPersonService) {
        this.companyPersonService = companyPersonService;
    }

    @PostMapping("")
    public ResponseEntity<?> assignPersonToCompany(@RequestParam Long personId, @RequestParam Long companyId) {
        try {
            CompanyPerson companyPerson = companyPersonService.assignPersonToCompany(personId, companyId);
            return new ResponseEntity<>(companyPerson, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /* Get all companies person is connected to */
    @GetMapping("")
    public ResponseEntity<?> getPersonCompanies(@RequestParam Long personId) {
        try {
            List<CompanyPerson> companies = companyPersonService.getAllCompaniesByPerson(personId);
            return new ResponseEntity<>(companies, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> removePersonFromCompany(@RequestParam Long personId, @RequestParam Long companyId) {
        try {
            ApiResponse response = companyPersonService.removePersonFromCompany(personId, companyId);
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
