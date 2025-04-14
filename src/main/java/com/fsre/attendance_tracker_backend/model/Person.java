package com.fsre.attendance_tracker_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String phone;

    @Column(nullable = false)
    private boolean isAdmin;

    @Column(nullable = false)
    private boolean isCompanyAdmin;

    @Column(nullable = false)
    private boolean isSupervisor;

    @Column(nullable = false)
    private boolean isWorker;


    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<CompanyPerson> companyPersons;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<DepartmentPerson> departmentPersons;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<ShiftAttendance> shiftAttendances;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<WorkShift> workShifts;
}
