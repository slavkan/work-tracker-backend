package com.fsre.attendance_tracker_backend.service;

import com.fsre.attendance_tracker_backend.model.Company;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.repo.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepo companyRepo;

    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    public Company getCompany (Long id) {
        return companyRepo.findById(id).orElse(null);
    }

    public Company addCompany(Company faculty) {
        return companyRepo.save(faculty);
    }

    public Company updateCompany(Long id, Company company) {
        Optional<Company> existingCompanyOptional = companyRepo.findById(id);
        if (existingCompanyOptional.isPresent()) {
            Company existingCompany = existingCompanyOptional.get();

            existingCompany.setName(company.getName());
            existingCompany.setAbbreviation(company.getAbbreviation());

            return companyRepo.save(existingCompany);
        } else {
            throw new RuntimeException("Company not found with id " + id);
        }
    }

    public ApiResponse deleteCompany(Long id) {
        Optional<Company> existingCompanyOptional = companyRepo.findById(id);
        if (existingCompanyOptional.isPresent()) {
            try {
                companyRepo.deleteById(id);
                return new ApiResponse("Company deleted successfully");
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Cannot delete company due to foreign key constraints");
            }
        } else {
            throw new RuntimeException("Company not found with id " + id);
        }
    }


}
