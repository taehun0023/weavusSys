package com.example.weavuscalculator.repo;

import com.example.weavuscalculator.entity.Accrual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccrualRepository extends JpaRepository<Accrual, Long> {
    Optional<Accrual> findByEmployeeId(String id);
}

