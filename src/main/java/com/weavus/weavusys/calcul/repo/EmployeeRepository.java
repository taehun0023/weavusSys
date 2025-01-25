package com.weavus.weavusys.calcul.repo;

import com.weavus.weavusys.calcul.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    List<Employee> findByStatus(int i);

    Optional<Employee> findById(String id);

    @Query("SELECT e FROM Employee e WHERE e.employeeType = 'REGULAR' AND e.conversionDate <= :endDate")
    List<Employee> findByIsRegularTrueAndPromotionDateBeforeOrEqualTo(@Param("endDate") LocalDate endDate);

}

