package com.fsre.attendance_tracker_backend.controller;

import com.fsre.attendance_tracker_backend.model.ShiftAttendance;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.service.ShiftAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/work-attendance")
@CrossOrigin
public class WorkAttendanceController {

    @Autowired
    private ShiftAttendanceService shiftAttendanceService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getShiftAttendancesForSession(@PathVariable Long id) {
        try {
            Iterable<ShiftAttendance> attendances = shiftAttendanceService.getClassAttendancesForSession(id);
            return new ResponseEntity<>(attendances, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/worker-arrival")
    public ResponseEntity<?> workerArrival(@RequestParam Long classSessionId, @RequestParam Long personId, @RequestParam String code) {
        try{
            ShiftAttendance classAttendance = shiftAttendanceService.workerToAttendance(classSessionId, personId, code);
            return new ResponseEntity<>(classAttendance, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
