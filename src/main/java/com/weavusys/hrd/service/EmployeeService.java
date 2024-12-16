package com.weavusys.hrd.service;

import com.weavusys.hrd.entity.Accrual;
import com.weavusys.hrd.entity.Employee;
import com.weavusys.hrd.repo.AccrualRepository;
import com.weavusys.hrd.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccrualRepository accrualRepository;
    public List<Employee> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    public boolean save(Employee employee) {
        employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.findById(employee.getId()).orElseThrow(() -> new RuntimeException("Employee not found"));
        Accrual accrual = new Accrual();
        accrual.setEmployee(savedEmployee);
            if (savedEmployee.getEmployeeType() == Employee.EmployeeType.REGULAR) {
               accrual.setStartDate(savedEmployee.getConversionDate());
            }
            accrualRepository.save(accrual);

            return true;
    }

    public Optional<Employee> findById(String id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee;
    }
}
