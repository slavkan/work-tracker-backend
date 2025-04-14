package com.fsre.attendance_tracker_backend.controller;

import com.fsre.attendance_tracker_backend.model.WorkShift;
import com.fsre.attendance_tracker_backend.model.dto.ApiResponse;
import com.fsre.attendance_tracker_backend.service.WorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/work-shifts")
@CrossOrigin
public class WorkShiftController {


    @Autowired
    private WorkShiftService workShiftService;


    @GetMapping("")
    public ResponseEntity<?> getWorkShiftsByDepartment(@RequestParam Long departmentId) {
        try{
            List<WorkShift> workShifts = workShiftService.getAllWorkSessionsByDepartment(departmentId);
            return new ResponseEntity<>(workShifts, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkShiftById(@PathVariable Long id) {
        try{
            Optional<WorkShift> classSession = workShiftService.getWorkShiftById(id);
            return new ResponseEntity<>(classSession, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/start")
    public ResponseEntity<?> startNewWorkShift(@RequestParam Long departmentId, @RequestParam Long supervisorId) {
        try {
            WorkShift classSession = workShiftService.startNewWorkShift(departmentId, supervisorId);
            return new ResponseEntity<>(classSession, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/end")
    public ResponseEntity<?> endWorkShift(@RequestParam Long workShiftId, @RequestParam(required = false) boolean nullifyUnfinishedAttendances) {
        try {
            WorkShift classSession = workShiftService.endWorkShift(workShiftId, nullifyUnfinishedAttendances);
            return new ResponseEntity<>(classSession, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change-qr-code")
    public ResponseEntity<?> changeQRCode(@RequestParam Long workShiftId, @RequestParam String newCode) {
        try {
            WorkShift classSession = workShiftService.changeQRCode(workShiftId, newCode);
            return new ResponseEntity<>(classSession, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/set-offset")
    public ResponseEntity<?> setOffset(@RequestParam Long workShiftId, @RequestParam Long offset) {
        try {
            WorkShift classSession = workShiftService.setOffset(workShiftId, offset);
            return new ResponseEntity<>(classSession, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


}
