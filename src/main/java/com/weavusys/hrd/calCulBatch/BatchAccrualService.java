package com.weavusys.hrd.calCulBatch;

import com.weavusys.hrd.entity.Accrual;
import com.weavusys.hrd.entity.Amount;
import com.weavusys.hrd.entity.Employee;
import com.weavusys.hrd.entity.MonthLog;
import com.weavusys.hrd.repo.AccrualRepository;
import com.weavusys.hrd.repo.MonthLogRepository;
import com.weavusys.hrd.repo.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class BatchAccrualService {
    @Autowired
    private AccrualRepository accrualRepository;
    private final SettingsRepository settingsRepository;
    private final MonthLogRepository monthLogRepository;

    @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 자정 실행
    public void scheduleBatchAccrual() {
        calculateTotalAccrual();
    }
    public void calculateTotalAccrual() {

        List<Accrual> accrualList = accrualRepository.findAll();
        LocalDate now = LocalDate.now();
        YearMonth nowMonth = YearMonth.from(now);
        MonthLog monthLog = new MonthLog();
        long monthTotal = 0;


        for (Accrual accrual : accrualList) {
            //직급 별로 월 적립금 금액 취득
            Employee employee = accrual.getEmployee();
            Amount setPrice = settingsRepository.findByRank(employee.getRank());

            LocalDate startDate = accrual.getStartDate() != null ? accrual.getStartDate() : now;
            YearMonth startMonth = YearMonth.from(startDate);

            LocalDate endDate = accrual.getEndDate() != null ? accrual.getEndDate() : now;
            YearMonth endMonth = YearMonth.from(endDate);

            long months = ChronoUnit.MONTHS.between(startMonth, endMonth);
            //퇴직금 지급 가능 여부 판단
            int state;
            if (months > 12) {
                if (endDate.getYear() < now.getYear()) {
                    state = 2;
                } else {
                    state = 1;
                }
            } else {
                state = 0;
            }
            accrual.setState(state);

            if (!employee.getEmployeeType().equals(Employee.EmployeeType.CONTRACT)) {
                long result = 0;

                // 적립금 계산
                if (accrual.getTotalAmount() == null) {
                    long totalMonths = endMonth.isAfter(nowMonth)
                            ? ChronoUnit.MONTHS.between(startMonth, nowMonth) + 1
                            : ChronoUnit.MONTHS.between(startMonth, endMonth) + 1;
                    result = totalMonths * setPrice.getMonthlyAmount();
                    monthTotal += result;
                } else if(accrual.getEndDate() != null){
                    if (nowMonth.isAfter(endMonth) || nowMonth.equals(endMonth)) {
                        result = accrual.getTotalAmount();
                    } else {
                        result = accrual.getTotalAmount() + setPrice.getMonthlyAmount();
                        monthTotal += setPrice.getMonthlyAmount();
                    }
                } else {
                    result = accrual.getTotalAmount() + setPrice.getMonthlyAmount();
                    monthTotal += setPrice.getMonthlyAmount();
                }

                // 적립금 설정
                accrual.setTotalAmount(result);
                accrualRepository.save(accrual);
            }
        }

        monthLog.setMonthlyTotal(monthTotal);
        monthLog.setSaveDate(now);
        monthLogRepository.save(monthLog);
    }

    public long calculateYearlyAccrualTotal(int year) {
        long yearlyTotal = 0;
        // 특정 연도의 모든 월로그 조회
        List<MonthLog> monthLogs = monthLogRepository.findBySaveDateYear(year);
        // 각 월별 퇴직금 총액 합산
        for (MonthLog monthLog : monthLogs) {
            yearlyTotal += monthLog.getMonthlyTotal();
        }
        return yearlyTotal;
    }
}
