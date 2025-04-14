package com.fsre.attendance_tracker_backend.service;

import com.fsre.attendance_tracker_backend.model.Department;
import com.fsre.attendance_tracker_backend.model.WorkShift;
import com.fsre.attendance_tracker_backend.model.Person;
import com.fsre.attendance_tracker_backend.repo.ShiftAttendanceRepo;
import com.fsre.attendance_tracker_backend.repo.WorkShiftRepo;
import com.fsre.attendance_tracker_backend.repo.PersonRepo;
import com.fsre.attendance_tracker_backend.repo.DepartmentRepo;
import com.fsre.attendance_tracker_backend.model.WorkShiftState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WorkShiftService {

    @Autowired
    private WorkShiftRepo workShiftRepo;

    @Autowired
    private ShiftAttendanceRepo shiftAttendanceRepo;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private PersonRepo personRepo;


    public List<WorkShift> getAllWorkSessionsByDepartment(Long departmentId) {
        return Optional.ofNullable(workShiftRepo.findByDepartmentId(departmentId)).orElse(Collections.emptyList());
    }

    public Optional<WorkShift> getWorkShiftById(Long id) {
        return workShiftRepo.findById(id);
    }

    public WorkShift startNewWorkShift(Long departmentId, Long supervisor_id) {
        Department department = departmentRepo.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found"));
        Person supervisor = personRepo.findById(supervisor_id).orElseThrow(() -> new RuntimeException("Supervisor not found"));
        if (!supervisor.isSupervisor()) {
            throw new RuntimeException("Person is not a supervisor");
        }

        WorkShift workShift = new WorkShift();
        workShift.setDepartment(department);
        workShift.setPerson(supervisor);
        workShift.setStartTime(LocalDateTime.now());
        workShift.setState(WorkShiftState.IN_PROGRESS);
        workShift.setOffsetInMinutes(0L);

        return workShiftRepo.save(workShift);
    }

    public WorkShift endWorkShift(Long workShiftId, boolean nullifyUnfinishedAttendances) {
        Optional<WorkShift> existingWorkShiftOptional = workShiftRepo.findById(workShiftId);
        if (existingWorkShiftOptional.isPresent()) {
            Long offset = existingWorkShiftOptional.get().getOffsetInMinutes();
            WorkShift existingWorkShift = existingWorkShiftOptional.get();
            existingWorkShift.setEndTime(LocalDateTime.now().plusMinutes(offset));
            existingWorkShift.setState(WorkShiftState.ENDED);

            /* If true, everyone who hasn't departed by scanning code to exit, it will be set like he was
            one minute in session */
            if (nullifyUnfinishedAttendances) {
                shiftAttendanceRepo.findByWorkShiftId(workShiftId).forEach(shiftAttendance -> {
                    if (shiftAttendance.getDepartureTime() == null) {
                        shiftAttendance.setDepartureTime(LocalDateTime.now());
                        shiftAttendance.setDepartureTime(shiftAttendance.getArrivalTime().plusMinutes(1));
                        shiftAttendanceRepo.save(shiftAttendance);
                    }
                });
            } else {
                shiftAttendanceRepo.findByWorkShiftId(workShiftId).forEach(shiftAttendance -> {
                    if (shiftAttendance.getDepartureTime() == null) {
                        shiftAttendance.setDepartureTime(LocalDateTime.now().plusMinutes(offset));
                        shiftAttendanceRepo.save(shiftAttendance);
                    }
                });
            }

            return workShiftRepo.save(existingWorkShift);
        } else {
            throw new RuntimeException("Work shift not found with id " + workShiftId);
        }
    }

    public WorkShift changeQRCode(Long workShiftId, String qrCode) {
        Optional<WorkShift> existingWorkShiftOptional = workShiftRepo.findById(workShiftId);
        if (existingWorkShiftOptional.isPresent()) {
            WorkShift existingWorkShift = existingWorkShiftOptional.get();
            existingWorkShift.setCodeForArrivalPrevious(existingWorkShift.getCodeForArrival());
            existingWorkShift.setCodeForArrival(qrCode);
            return workShiftRepo.save(existingWorkShift);
        } else {
            throw new RuntimeException("Work shift not found with id " + workShiftId);
        }
    }

    public WorkShift setOffset(Long workShiftId, Long offset) {
        Optional<WorkShift> existingWorkShiftOptional = workShiftRepo.findById(workShiftId);
        if (existingWorkShiftOptional.isPresent()) {
            WorkShift existingWorkShift = existingWorkShiftOptional.get();
            existingWorkShift.setOffsetInMinutes(offset);
            return workShiftRepo.save(existingWorkShift);
        } else {
            throw new RuntimeException("Work shift not found with id " + workShiftId);
        }
    }


}
