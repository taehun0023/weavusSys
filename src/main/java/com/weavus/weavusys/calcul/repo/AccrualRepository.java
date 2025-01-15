package com.weavus.weavusys.calcul.repo;

import com.weavus.weavusys.calcul.entity.Accrual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccrualRepository extends JpaRepository<Accrual, Long> {
    Accrual findByEmployeeId(String id);
}

