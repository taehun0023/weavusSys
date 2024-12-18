package com.weavusys.hrd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Amount {

    @Id
    private Long rank;  //직급 고유값
    private Integer monthlyAmount;
}
