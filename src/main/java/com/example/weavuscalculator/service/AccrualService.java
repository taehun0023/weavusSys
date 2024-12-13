package com.example.weavuscalculator.service;

import com.example.weavuscalculator.entity.Accrual;
import com.example.weavuscalculator.entity.Employee;
import com.example.weavuscalculator.repo.AccrualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccrualService {

    private final AccrualRepository accrualRepository;


    public int calculateTotalAccrual(Accrual accrual) {
        YearMonth startMonth = YearMonth.from(accrual.getStartDate());
        LocalDate endDate = accrual.getEndDate() != null ? accrual.getEndDate() : LocalDate.now();
        YearMonth endMonth = YearMonth.from(endDate);
        long months = ChronoUnit.MONTHS.between(startMonth, endMonth);

        if (endDate.getDayOfMonth() < endDate.lengthOfMonth()) {
            months -= 1; // 4월 제외
        }

        if (endDate.getDayOfMonth() == 1) {
            months += 1; // 종료일이 1일일 때, 그 달을 추가
        }

        int result = (int) months * 5000;
        accrual.setTotalAmount(result);
        accrualRepository.save(accrual);
        return result;
    }

    public Optional<Accrual> findByEmployeeId(String id) {
        Optional<Accrual> accrual = accrualRepository.findByEmployeeId(id);
        return accrual;
    }

    public List<Accrual> findAll() {
        List<Accrual> accrualList = accrualRepository.findAll();
        for (Accrual accrual : accrualList ){
            if (!accrual.getEmployee().getEmployeeType().equals(Employee.EmployeeType.CONTRACT)){
//                if (accrual.getTotalAmount() == null || accrual.getTotalAmount() == 0) {
                    // totalAmount 값을 설정 (예시로 100으로 설정)
                    accrual.setTotalAmount(calculateTotalAccrual(accrual));
//                }
            }
        }
        return accrualList;
    }
}
