package com.fsre.attendance_tracker_backend.service;

import com.fsre.attendance_tracker_backend.model.Company;
import com.fsre.attendance_tracker_backend.model.CompanyPerson;
import com.fsre.attendance_tracker_backend.model.Person;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.repo.CompanyPersonRepo;
import com.fsre.attendance_tracker_backend.repo.CompanyRepo;
import com.fsre.attendance_tracker_backend.repo.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyPersonService {

    @Autowired
    private CompanyPersonRepo companyPersonRepo;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private CompanyRepo companyRepo;


    public CompanyPerson getCompanyPerson (Long id) {
        return companyPersonRepo.findById(id).orElse(null);
    }

    public CompanyPerson assignPersonToCompany(Long personId, Long companyId) {
        Person person = personRepo.findById(personId).orElseThrow(() -> new RuntimeException("Person not found"));
        Company company = companyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        if (companyPersonRepo.findByPersonIdAndCompanyId(personId, companyId).isPresent()) {
            throw new RuntimeException("Person and company are already connected");
        }

        CompanyPerson companyPerson = new CompanyPerson();
        companyPerson.setPerson(person);
        companyPerson.setCompany(company);

        return companyPersonRepo.save(companyPerson);
    }


    public List<CompanyPerson> getAllCompaniesByPerson(Long personId) {
        return companyPersonRepo.findByPersonId(personId);
    }

    public ApiResponse removePersonFromCompany(Long personId, Long companyId) {
        Optional<CompanyPerson> existingCompanyPersonOptional = companyPersonRepo.findByPersonIdAndCompanyId(personId, companyId);
        if (existingCompanyPersonOptional.isPresent()) {
            try {
                companyPersonRepo.delete(existingCompanyPersonOptional.get());
                return new ApiResponse("User removed from company");
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Cannot remove user from company due to foreign key constraints");
            }
        } else {
            throw new RuntimeException("User and company connection not found");
        }
    }

}
