package com.weavus.weavusys.calcul.calCulBatch;

import com.weavus.weavusys.calcul.entity.Accrual;
import com.weavus.weavusys.calcul.entity.Amount;
import com.weavus.weavusys.calcul.entity.Employee;
import com.weavus.weavusys.calcul.entity.MonthLog;
import com.weavus.weavusys.calcul.repo.AccrualRepository;
import com.weavus.weavusys.calcul.repo.EmployeeRepository;
import com.weavus.weavusys.calcul.repo.MonthLogRepository;
import com.weavus.weavusys.calcul.repo.SettingsRepository;
import lombok.RequiredArgsConstructor;
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

    private final AccrualRepository accrualRepository;
    private final SettingsRepository settingsRepository;
    private final MonthLogRepository monthLogRepository;

    private final EmployeeRepository employeeRepository;

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

            LocalDate startDate = employee.getConversionDate() != null ? employee.getConversionDate() : now;
            YearMonth startMonth = YearMonth.from(startDate);

            LocalDate endDate = employee.getExitDate() != null ? employee.getExitDate() : now;
            YearMonth endMonth = YearMonth.from(endDate);

            long months = ChronoUnit.MONTHS.between(startMonth, endMonth);
            //퇴직금 지급 가능 여부 판단
            int state = (months > 12) ? (endDate.getYear() < now.getYear() ? 2 : 1) : 0;
            accrual.setState(state);
            // month 12개월 이하 시 0 값을 state에 부여,
            // month 12개월 초과 시 now와 endDate를 비교하여 2, 1값 을 state에 부여
            // 0 : 지급 불가능, 1 : 지금 가능, 2 : 지급 완료

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
                } else if(employee.getExitDate() != null){
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
        LocalDate endDate = LocalDate.of(year, 12, 31); // 해당 연도의 마지막 날

        List<MonthLog> monthLogs = monthLogRepository.findBySaveDateYear(year);
        if (!monthLogs.isEmpty()) {
            // MonthLog에 데이터가 있는 경우, 해당 데이터로 계산
            for (MonthLog monthLog : monthLogs) {
                yearlyTotal += monthLog.getMonthlyTotal();
            }
        } else {
            // MonthLog에 데이터가 없는 경우, 기존 로직으로 계산
            List<Employee> regularEmployees = employeeRepository.findByIsRegularTrueAndPromotionDateBeforeOrEqualTo(endDate);

            for (Employee employee : regularEmployees) {
                Amount setPrice = settingsRepository.findByRank(employee.getRank());
                LocalDate promotionDate = employee.getConversionDate(); // 정직원 전환일

                // 정직원 전환일 이후 퇴직금 계산
                if (promotionDate.isBefore(endDate) || promotionDate.isEqual(endDate)) {
                    // 전환일부터 해당 연도까지의 개월 수 계산
                    long monthsBetween = ChronoUnit.MONTHS.between(promotionDate.withDayOfMonth(1), endDate.withDayOfMonth(1));

                    yearlyTotal += monthsBetween * setPrice.getMonthlyAmount();
                }
            }
        }

        return yearlyTotal;

    }
}
