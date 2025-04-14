package com.fsre.attendance_tracker_backend.repo.specification;

import com.fsre.attendance_tracker_backend.model.Person;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecifications {

    public static Specification<Person> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null) {
                return null;
            }
            String pattern = "%" + firstName + "%";
            return criteriaBuilder.like(root.get("firstName"), pattern);
        };
    }

    public static Specification<Person> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null) {
                return null;
            }
            String pattern = "%" + lastName + "%";
            return criteriaBuilder.like(root.get("lastName"), pattern);
        };
    }

    public static Specification<Person> hasRole(String role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) {
                return null;
            }
            switch (role) {
                case "admin":
                    return criteriaBuilder.isTrue(root.get("isAdmin"));
                case "companyAdmin":
                    return criteriaBuilder.isTrue(root.get("isCompanyAdmin"));
                case "supervisor":
                    return criteriaBuilder.isTrue(root.get("isSupervisor"));
                case "worker":
                    return criteriaBuilder.isTrue(root.get("isWorker"));
                default:
                    return null;
            }
        };
    }

    public static Specification<Person> hasCompanyId(Long companyId) {
        return (root, query, criteriaBuilder) -> {
            if (companyId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.join("companyPersons").get("company").get("id"), companyId);
        };
    }

}
