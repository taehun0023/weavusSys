package com.weavus.weavusys.calcul.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Employee {

    @Id
    @Column(nullable = false)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate entryDate;
    private LocalDate exitDate;
    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;
    private LocalDate conversionDate;
    @Column(nullable = false)
    private Integer rank; //직급 추가
    private Integer status; //유저 활성화, 비활성화

    public enum EmployeeType {
        REGULAR, CONTRACT
    }

}
