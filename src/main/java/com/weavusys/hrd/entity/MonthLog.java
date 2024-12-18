package com.weavusys.hrd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class MonthLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //직급 고유값
    private Long monthlyTotal;
    private LocalDate saveDate;
}
