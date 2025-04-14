package com.fsre.attendance_tracker_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;



    /**/

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<DepartmentPerson> departmentPersons;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<WorkShift> workShifts;
}
