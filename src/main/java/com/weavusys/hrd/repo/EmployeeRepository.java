package com.weavusys.hrd.repo;

import com.weavusys.hrd.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByStatus(int i);

    Optional<Employee> findById(String id);
}

