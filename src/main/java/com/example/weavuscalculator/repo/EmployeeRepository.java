package com.example.weavuscalculator.repo;

import com.example.weavuscalculator.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findById(String id);
}

