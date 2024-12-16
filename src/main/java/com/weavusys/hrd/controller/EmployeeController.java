package com.weavusys.hrd.controller;

import com.weavusys.hrd.entity.Accrual;
import com.weavusys.hrd.entity.Employee;
import com.weavusys.hrd.repo.EmployeeRepository;
import com.weavusys.hrd.service.AccrualService;
import com.weavusys.hrd.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
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
        if(employeeService.save(employee)){
            return "회원등록이 완료되었습니다.";
        }
        return "다시 한번 입력해 주세요.";
    }

    @GetMapping("/{id}/accrual")
            public Accrual getAccrual(@PathVariable String id) {
        Accrual accrual = accrualService.findByEmployeeId(id).orElseThrow();
        accrualService.calculateTotalAccrual(accrual);
        return accrual;
    }
}