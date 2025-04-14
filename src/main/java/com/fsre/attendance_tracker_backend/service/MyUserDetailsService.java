package com.fsre.attendance_tracker_backend.service;

import com.fsre.attendance_tracker_backend.model.Person;
import com.fsre.attendance_tracker_backend.model.UserPrincipal;
import com.fsre.attendance_tracker_backend.repo.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonRepo personRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepo.findByUsername(username);
        if (person == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }

        return new UserPrincipal(person);
    }
}