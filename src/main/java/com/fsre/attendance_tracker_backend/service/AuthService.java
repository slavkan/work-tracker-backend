package com.fsre.attendance_tracker_backend.service;

import com.fsre.attendance_tracker_backend.model.Person;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.model.dto.LoginRequestDto;
import com.fsre.attendance_tracker_backend.repo.PersonRepo;
import com.fsre.attendance_tracker_backend.utils.PasswordGeneratorUtil;
import com.fsre.attendance_tracker_backend.utils.PasswordValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private EmailService emailService;

    /*public ApiResponse verify(LoginRequestDto loginRequestDto) {
        try {
            // Check if the user exists
            Person person = personRepo.findByUsername(loginRequestDto.getUsername());
            if (person == null) {
                return new ApiResponse("User not found");
            }

            // Attempt authentication
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                return new ApiResponse(jwtService.generateToken(loginRequestDto.getUsername(), userDetails.getAuthorities()));
            } else {
                return new ApiResponse("Failed to verify user");
            }
        } catch (BadCredentialsException e) {
            return new ApiResponse("Incorrect password");
        } catch (AuthenticationException e) {
            return new ApiResponse("Invalid username or password");
        }
    }*/

    public String verify(LoginRequestDto loginRequestDto) {
        // Check if the user exists
        Person person = personRepo.findByUsername(loginRequestDto.getUsername());
        if (person == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Attempt authentication
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtService.generateToken(loginRequestDto.getUsername(), userDetails.getAuthorities(), person.getId());
        } else {
            throw new BadCredentialsException("Failed to verify user");
        }
    }

    public Person changePassword(Long id, String newPassword) {
        Optional<Person> existingPersonOptional = personRepo.findById(id);
        if (existingPersonOptional.isPresent()) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Person person = personRepo.findByUsername(userDetails.getUsername());
            if (!person.getId().equals(id)) {
                throw new RuntimeException("You can only change your own password");
            }

            try {
                PasswordValidatorUtil.validatePassword(newPassword);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Password error: " + e.getMessage());
            }

            Person existingPerson = existingPersonOptional.get();
            existingPerson.setPassword(encoder.encode(newPassword));
            return personRepo.save(existingPerson);
        } else {
            throw new RuntimeException("Person not found with id " + id);
        }
    }

    public Person generateNewPassword(Long id) {
        Optional<Person> existingPersonOptional = personRepo.findById(id);
        if (existingPersonOptional.isPresent()) {
            String generatedPassword = PasswordGeneratorUtil.generateNewPassword();
            Person existingPerson = existingPersonOptional.get();
            existingPerson.setPassword(encoder.encode(generatedPassword));

            /* Comment to not send emails in development, console log instead */
            System.out.println("GENERATED PASSWORD: " + generatedPassword);
            emailService.sendCredentialsEmail(
                    existingPerson.getEmail(),
                    existingPerson.getUsername(),
                    generatedPassword
            );

            return personRepo.save(existingPerson);
        } else {
            throw new RuntimeException("Person not found with id " + id);
        }
    }


}
