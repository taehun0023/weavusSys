package com.weavusys.hrd.service;

import com.weavusys.hrd.entity.Accrual;
import com.weavusys.hrd.entity.Employee;
import com.weavusys.hrd.repo.AccrualRepository;
import com.weavusys.hrd.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccrualRepository accrualRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public List<Employee> findAll() {
        return employeeRepository.findByStatus(0);
    }

    public String save(Employee employee) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = employee.getConversionDate() != null ? employee.getConversionDate() : now;
        YearMonth startMonth = YearMonth.from(startDate);
        LocalDate endDate = employee.getExitDate() != null ? employee.getExitDate() : now;
        YearMonth endMonth = YearMonth.from(endDate);
        long months = ChronoUnit.MONTHS.between(startMonth, endMonth);
        int state = (months > 12) ? (endDate.getYear() < now.getYear() ? 2 : 1) : 0;

        if (employeeRepository.findById(employee.getId()).isEmpty()){
            try {
                employee.setStatus(0);
                employeeRepository.save(employee);
                Accrual accrual = new Accrual();
                accrual.setEmployee(employee);
                accrual.setState(state);
                if(employee.getEmployeeType().equals(Employee.EmployeeType.REGULAR)){
                    accrual.setStartDate(employee.getConversionDate());
                }
                if (employee.getExitDate() != null){
                    accrual.setEndDate(employee.getExitDate());
                }
                accrualRepository.save(accrual);
                return "0";
            }catch (Exception e){
                logger.error("저장 실패", e);
                return "1";
            }
        }
        logger.error("아이디 중복");
        return "2";
    }

    public Employee modifyEmployee(String id, Employee employee) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee user = existingEmployee.get();
            user.setId(employee.getId());
            user.setName(employee.getName());
            user.setEntryDate(employee.getEntryDate());
            user.setExitDate(employee.getExitDate());
            user.setEmployeeType(employee.getEmployeeType());
            user.setConversionDate(employee.getConversionDate());
            user.setRank(employee.getRank());
            return employeeRepository.save(user);
        }
        return null;
    }

    public boolean deleteById(String id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        optionalEmployee.ifPresent(employee -> {
//            employee.setId(id + "_notUsed");
            employee.setStatus(1);
            employeeRepository.save(employee);
        });
        return optionalEmployee.isPresent();
    }

    public List<Employee> findByEntryDateYear(Integer year) {
        return employeeRepository.findByEntryDateYear(year);
    }



//    public boolean save(Employee employee) {
//        employeeRepository.save(employee);
//        Employee savedEmployee = employeeRepository.findById(employee.getId()).orElseThrow(() -> new RuntimeException("Employee not found"));
//        Accrual accrual = new Accrual();
//        accrual.setEmployee(savedEmployee);
//            if (savedEmployee.getEmployeeType() == Employee.EmployeeType.REGULAR) {
//               accrual.setStartDate(savedEmployee.getConversionDate());
//            }
//            accrualRepository.save(accrual);
//
//            return true;
//    }

}
