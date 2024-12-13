package com.example.weavuscalculator.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Accrual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalAmount;

//    @PrePersist
//    public void setStartDate() {
//        if (employee != null && employee.getEmployeeType() == Employee.EmployeeType.REGULAR) {
//            this.startDate = employee.getEntryDate();
//            this.endDate = null; // endDate 초기화
//            this.totalAmount = null; // totalAmount 초기화
//        }
//    }

}

