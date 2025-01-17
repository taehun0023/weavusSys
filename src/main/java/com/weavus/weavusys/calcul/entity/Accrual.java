package com.weavus.weavusys.calcul.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    //employee 테이블과 endDate, startDate 중복으로 생략

    private Long totalAmount;
    private Integer state;

}

