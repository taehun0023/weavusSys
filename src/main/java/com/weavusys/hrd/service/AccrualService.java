package com.weavusys.hrd.service;

import com.weavusys.hrd.entity.Accrual;
import com.weavusys.hrd.entity.Amount;
import com.weavusys.hrd.entity.Employee;
import com.weavusys.hrd.repo.AccrualRepository;
import com.weavusys.hrd.repo.SettingsRepository;
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
    private final SettingsRepository settingsRepository;

    public Long calculateTotalAccrual(Accrual accrual) {
        Amount setPrice = settingsRepository.findById(1);
        Employee employee = accrual.getEmployee();

        YearMonth startMonth = YearMonth.from(employee.getConversionDate());
        LocalDate endDate = accrual.getEndDate() != null ? accrual.getEndDate() : LocalDate.now();
        YearMonth endMonth = YearMonth.from(endDate);
        long months = ChronoUnit.MONTHS.between(startMonth, endMonth);
        if(months >= 12){
            accrual.setStartDate(employee.getConversionDate().plusMonths(12));
            startMonth = YearMonth.from(accrual.getStartDate());
            months = ChronoUnit.MONTHS.between(startMonth, endMonth);

            if (endDate.getDayOfMonth() < endDate.lengthOfMonth()) {
                months -= 1; // 4월 제외
            }

            if (endDate.getDayOfMonth() == 1) {
                months += 1; // 종료일이 1일일 때, 그 달을 추가
            }

            long result = 0;
            switch (1) {
                case 3:
                     result = months * setPrice.getMonthlyAmount();
                    break;
                case 2:
                     result = months * setPrice.getMonthlyAmount();
                    break;
                case 1:
                     result = months * setPrice.getMonthlyAmount();
                    break;
                default:
                     result = months * setPrice.getMonthlyAmount();
                    break;
            }
//            long result = months * setPrice.getMonthlyAmount();
            accrual.setTotalAmount(result);
            accrualRepository.save(accrual);
            return result;
        }
        return null;
    }

    public Optional<Accrual> findByEmployeeId(String id) {
        Optional<Accrual> accrual = accrualRepository.findByEmployeeId(id);
        return accrual;
    }

    public List<Accrual> findAll() {
        List<Accrual> accrualList = accrualRepository.findAll();
        for (Accrual accrual : accrualList ){
            if (!accrual.getEmployee().getEmployeeType().equals(Employee.EmployeeType.CONTRACT)){
                    accrual.setTotalAmount(calculateTotalAccrual(accrual));
            }
        }
        return accrualList;
    }
}
