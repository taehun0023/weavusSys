package com.weavusys.hrd.repo;

import com.weavusys.hrd.entity.Accrual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccrualRepository extends JpaRepository<Accrual, Long> {
    Accrual findByEmployeeId(String id);
}

