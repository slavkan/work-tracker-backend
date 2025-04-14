package com.fsre.attendance_tracker_backend.controller;

import com.fsre.attendance_tracker_backend.model.*;
import com.fsre.attendance_tracker_backend.repo.*;
import com.fsre.attendance_tracker_backend.service.ShiftAttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fsre.attendance_tracker_backend.model.dto.WorkerArrivalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ShiftAttendanceService classAttendanceService;

    @Autowired
    private ShiftAttendanceRepo classAttendanceRepo;

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private WorkShiftRepo classSessionRepo;

    @Autowired
    private DepartmentPersonRepo subjectPersonRepo;

    Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        Logger logger = LoggerFactory.getLogger(ChatController.class);
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        return message;
    }



    @MessageMapping("/class-session")
    public WorkerArrivalDto receiveMessage(@Payload WorkerArrivalDto studentArrivalDto) {
        Long personId = studentArrivalDto.getPersonId();
        Long classSessionId = studentArrivalDto.getWorkShiftId();
        String code = studentArrivalDto.getCode();


        WorkerArrivalDto message = new WorkerArrivalDto();
        message.setWorkShiftId(studentArrivalDto.getWorkShiftId());
        message.setPersonId(personId);



        Person person = personRepo.findById(personId).orElse(null);
        if (person == null) {
            message.setFirstName("Unknown");
            message.setLastName("Unknown");
            message.setMessage("Person not found");
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private", message);
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private-student", message);
            return message;
        }
        message.setFirstName(person.getFirstName());
        message.setLastName(person.getLastName());

        WorkShift classSession = classSessionRepo.findById(classSessionId).orElse(null);
        if (classSession == null) {
            message.setMessage("Class Session not found");
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private", message);
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private-student", message);
            return message;
        }
        message.setDepartmentName(classSession.getDepartment().getName());

        Long subjectId = classSession.getDepartment().getId();
        if (subjectPersonRepo.findByPersonIdAndDepartmentId(personId, subjectId).isEmpty()) {
            message.setMessage("Worker is not part of this subject");
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private", message);
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private-student", message);
            return message;
        }
        if (classSession.getState() != WorkShiftState.IN_PROGRESS) {
            message.setMessage("Class session is not in progress");
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private", message);
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private-student", message);
            return message;
        }
        if (!Objects.equals(classSession.getCodeForArrival(), code) && !Objects.equals(classSession.getCodeForArrivalPrevious(), code)) {
            message.setMessage("Invalid code");
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private", message);
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private-student", message);
            return message;
        }

        Optional<ShiftAttendance> classAttendance = classAttendanceRepo.findByWorkShiftIdAndPersonId(classSessionId, personId);
        if (classAttendance.isPresent()) {
            ShiftAttendance attendance = classAttendance.get();
            if (attendance.getDepartureTime() != null) {
                message.setMessage("Worker already attended this class session");
                simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private", message);
                simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private-student", message);
                return message;
            } else {
                attendance.setDepartureTime(LocalDateTime.now().plusMinutes(classSession.getOffsetInMinutes()));
                classAttendanceRepo.save(attendance);
                message.setMessage("Worker has departed from class session");
                simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private", message);
                simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private-student", message);
                return message;
            }
        } else {
            message.setArrivalTime(LocalDateTime.now().toString());
            message.setMessage("Worker has arrived at class session");
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private", message);
            simpMessagingTemplate.convertAndSendToUser(studentArrivalDto.getWorkShiftId().toString(),"/private-student", message);
            ShiftAttendance attendance = new ShiftAttendance();
            attendance.setWorkShift(classSession);
            attendance.setPerson(person);
            attendance.setArrivalTime(LocalDateTime.now().plusMinutes(classSession.getOffsetInMinutes()));
            classAttendanceRepo.save(attendance);
            return message;
        }
    }



}

