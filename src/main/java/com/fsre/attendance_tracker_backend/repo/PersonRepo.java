package com.fsre.attendance_tracker_backend.repo;

import com.fsre.attendance_tracker_backend.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonRepo extends
        JpaRepository<Person, Long>,
        PagingAndSortingRepository<Person, Long>,
        JpaSpecificationExecutor<Person> {
    Person findByUsername(String username);

    Person findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
