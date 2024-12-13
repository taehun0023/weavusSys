package com.example.weavuscalculator.repo;

import com.example.weavuscalculator.entity.Amount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingsRepository extends JpaRepository<Amount, Integer> {
    Amount findById(int i);
}
