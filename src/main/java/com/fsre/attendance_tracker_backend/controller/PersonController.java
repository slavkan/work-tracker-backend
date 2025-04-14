package com.fsre.attendance_tracker_backend.controller;

import com.fsre.attendance_tracker_backend.model.Person;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.model.dto.PasswordChangeDto;
import com.fsre.attendance_tracker_backend.model.dto.PersonFilterDto;
import com.fsre.attendance_tracker_backend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
@CrossOrigin
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return new ResponseEntity<>(personService.getAllPersons(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable Long id) {
        Person person = personService.getPerson(id);
        if(person != null) {
            return new ResponseEntity<>(person, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addPerson(@RequestBody Person person) {
        try{
            Person person1 = personService.addPerson(person);
            return new ResponseEntity<>(person1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        try {
            Person updatedPerson = personService.updatePerson(id, person);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        try {
            ApiResponse response = personService.deletePerson(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("foreign key constraints")) {
                return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
            }
        }
    }

    /* FILTERING */
    @GetMapping("/filter")
    public ResponseEntity<Page<Person>> getFilteredPersons(
            @ModelAttribute PersonFilterDto filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Person> persons = personService.getFilteredPersons(filter, page, size);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }




}
