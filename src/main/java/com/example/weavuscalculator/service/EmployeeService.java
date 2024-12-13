package com.example.weavuscalculator.service;

import com.example.weavuscalculator.entity.Employee;
import com.example.weavuscalculator.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    public List<Employee> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    public boolean save(Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);
        if(savedEmployee == null){
            return true;
        }
        return false;
    }

    public Optional<Employee> findById(String id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee;
    }
}
