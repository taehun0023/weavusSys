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

    @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 자정 calculateTotalAccrual실행
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
            int state = (months > 12) ? (endDate.getYear() < now.getYear() ? 2 : 1) : 0;
            accrual.setState(state);
            // month 12개월 이하 시 0 값을 state에 부여,
            // month 12개월 초과 시 now와 endDate를 비교하여 2, 1값 을 state에 부여

            if (!employee.getEmployeeType().equals(Employee.EmployeeType.CONTRACT)) {
                long result = 0;

                // 적립금 계산
                if (accrual.getTotalAmount() == null) {
                    //총적립금이 없을 시 초기 계산 실시 (계약->정직원 전환자 대상)
                    long totalMonths = endMonth.isAfter(nowMonth)
                            ? ChronoUnit.MONTHS.between(startMonth, nowMonth) + 1
                            : ChronoUnit.MONTHS.between(startMonth, endMonth) + 1;
                    result = totalMonths * setPrice.getMonthlyAmount();
                    monthTotal += result;
                } else if(accrual.getEndDate() != null){
                    //총적립금과 퇴직일이 존재할 시 퇴직일 까지 월적립금 누적
                    if (nowMonth.isAfter(endMonth) || nowMonth.equals(endMonth)) {
                        result = accrual.getTotalAmount();
                    } else {
                        result = accrual.getTotalAmount() + setPrice.getMonthlyAmount();
                        monthTotal += setPrice.getMonthlyAmount();
                    }
                } else {
                    //총적립금은 존재하고 퇴직일은 없을 시 기존 적립금에 월적립금 누적
                    result = accrual.getTotalAmount() + setPrice.getMonthlyAmount();
                    monthTotal += setPrice.getMonthlyAmount();
                }

                // 계산된 적립금 저장
                accrual.setTotalAmount(result);
                accrualRepository.save(accrual);
            }
        }
        //월별 누적된 적립금 로그 저장
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
