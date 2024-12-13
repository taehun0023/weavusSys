package com.example.weavuscalculator.controller;

import com.example.weavuscalculator.entity.Accrual;
import com.example.weavuscalculator.entity.Employee;
import com.example.weavuscalculator.repo.EmployeeRepository;
import com.example.weavuscalculator.service.AccrualService;
import com.example.weavuscalculator.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://vwcvd4.csb.app/")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final AccrualService accrualService;
    private final EmployeeService employeeService;


    @GetMapping
    public List<Accrual> getAllEmployees() {
        return accrualService.findAll();
    }

    @PostMapping
    public String addEmployee(@RequestBody Employee employee) {
        if(!employeeService.save(employee)){
            return "회원등록이 완료되었습니다.";
        }
        return "다시 한번 입력해 주세요.";
    }

    @GetMapping("/{id}/accrual")
            public String getAccrual(@PathVariable String id) {
        Accrual accrual = accrualService.findByEmployeeId(id).orElseThrow();
        String result = String.valueOf(accrualService.calculateTotalAccrual(accrual));
        return result;
    }
}