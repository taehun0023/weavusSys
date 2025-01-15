package com.weavus.weavusys.calcul.repo;

import com.weavus.weavusys.calcul.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByStatus(int i);

    Optional<Employee> findById(String id);
}

