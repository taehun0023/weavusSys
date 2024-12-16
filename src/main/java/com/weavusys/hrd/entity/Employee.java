package com.weavusys.hrd.entity;

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
    private Integer rank; //직급 추가

    public enum EmployeeType {
        REGULAR, CONTRACT
        //이넘 벨류랑 네임으로 넣기 -> DB는 value, 표시는 네임
        //-> 서브 클래스가 아닌 따로 이넘을 클래스로따로 빼서 하는 경우가 많음
        //따른 곳에서 이넘패키지로 빼기
    }
}
