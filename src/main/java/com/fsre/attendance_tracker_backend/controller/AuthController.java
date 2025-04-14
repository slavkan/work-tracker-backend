package com.fsre.attendance_tracker_backend.controller;

import com.fsre.attendance_tracker_backend.model.Person;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.model.dto.LoginRequestDto;
import com.fsre.attendance_tracker_backend.model.dto.PasswordChangeDto;
import com.fsre.attendance_tracker_backend.service.AuthService;
import com.fsre.attendance_tracker_backend.service.PersonService;
import com.fsre.attendance_tracker_backend.utils.PasswordGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private PersonService personService;

    @Autowired
    private AuthService authService;


    /*@PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.verify(loginRequestDto);
    }*/

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            String token = authService.verify(loginRequestDto);
            return new ResponseEntity<>(new ApiResponse(token), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse("User not found"), HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ApiResponse("Incorrect password"), HttpStatus.UNAUTHORIZED);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ApiResponse("Invalid username or password"), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity<?> generateNewPassword(@PathVariable Long id, @RequestBody PasswordChangeDto passwordChange) {
        try {
            Person updatedPerson = authService.changePassword(id, passwordChange.getPassword());
            return new ResponseEntity<>(new ApiResponse("Password changed successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/generate-password/{id}")
    public ResponseEntity<?> generateNewPassword(@PathVariable Long id) {
        try {
            Person updatedPerson = authService.generateNewPassword(id);
            return new ResponseEntity<>("New password generated", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
