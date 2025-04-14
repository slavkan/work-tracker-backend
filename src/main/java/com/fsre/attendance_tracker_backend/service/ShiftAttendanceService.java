package com.fsre.attendance_tracker_backend.service;

import com.fsre.attendance_tracker_backend.model.*;
import com.fsre.attendance_tracker_backend.model.dto.WorkerArrivalDto;
import com.fsre.attendance_tracker_backend.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShiftAttendanceService {

    @Autowired
    private ShiftAttendanceRepo classAttendanceRepo;

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private WorkShiftRepo classSessionRepo;

    @Autowired
    private DepartmentPersonRepo subjectPersonRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;



    public Iterable<ShiftAttendance> getClassAttendancesForSession(Long shiftId) {
        classSessionRepo.findById(shiftId).orElseThrow(() -> new RuntimeException("Work shift not found"));
        return classAttendanceRepo.findByWorkShiftId(shiftId);
    }

    public ShiftAttendance workerToAttendance(Long workShiftId, Long personId, String code) {
        Person person = personRepo.findById(personId).orElseThrow(() -> new RuntimeException("Person not found"));
        WorkShift workShift = classSessionRepo.findById(workShiftId).orElseThrow(() -> new RuntimeException("Work shift not found"));

        Long subjectId = workShift.getDepartment().getId();

        if (subjectPersonRepo.findByPersonIdAndDepartmentId(personId, subjectId).isEmpty()) {
            throw new RuntimeException("Worker is not part of this department");
        }
        if(workShift.getState() != WorkShiftState.IN_PROGRESS) {
            throw new RuntimeException("Work shift is not in progress");
        }
        if(!Objects.equals(workShift.getCodeForArrival(), code) && !Objects.equals(workShift.getCodeForArrivalPrevious(), code)) {
            throw new RuntimeException("Invalid code");
        }

        /* Check if code is current one, if not then student has sent previous code (which also works) */
        boolean isCodeCurrent = Objects.equals(workShift.getCodeForArrival(), code);

        Optional<ShiftAttendance> classAttendance = classAttendanceRepo.findByWorkShiftIdAndPersonId(workShiftId, personId);
        /* If student has arrived at session */
        if (classAttendance.isPresent()) {
            ShiftAttendance attendance = classAttendance.get();


            if (attendance.getDepartureTime() != null) {
                /* If student has already ended his session */
                throw new RuntimeException("Worker already attended this class shift");
            } else {
                /* Student is ending his session now */
                attendance.setDepartureTime(LocalDateTime.now());
                return classAttendanceRepo.save(attendance);
            }
        } else {
            /* Student is starting his session now */
            WorkerArrivalDto workerArrivalDto = new WorkerArrivalDto();
            workerArrivalDto.setWorkShiftId(workShiftId);
            workerArrivalDto.setPersonId(personId);
            workerArrivalDto.setDepartmentName(workShift.getDepartment().getName());
            workerArrivalDto.setFirstName(person.getFirstName());
            workerArrivalDto.setLastName(person.getLastName());
            workerArrivalDto.setArrivalTime(LocalDateTime.now().toString());
            workerArrivalDto.setMessage("Worker has arrived at class session");

            simpMessagingTemplate.convertAndSend("/topic/class-session/" + workShiftId, workerArrivalDto);

            ShiftAttendance attendance = new ShiftAttendance();
            attendance.setWorkShift(workShift);
            attendance.setPerson(person);
            attendance.setArrivalTime(LocalDateTime.now());
            return classAttendanceRepo.save(attendance);
        }
    }

    /*test method just print hello world*/
    public void helloWorld(){
        System.out.println("Hello World");
    }

}
