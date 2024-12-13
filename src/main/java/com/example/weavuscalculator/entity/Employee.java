package com.example.weavuscalculator.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Employee {

    @Id
    private String id;

    private String name;
    private LocalDate entryDate;
    private LocalDate exitDate;

    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;

    private LocalDate conversionDate;

    public enum EmployeeType {
        REGULAR, CONTRACT
    }
}
