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
        String A = null;
        calculateTotalAccrual(A);
    }

    public void calculateTotalAccrual(String A) {
//        List<Accrual> accrualList = accrualRepository.findAll();
//        MonthLog monthLog = new MonthLog();
//        long monthTotal = 0;
//        for (Accrual accrual : accrualList ){
//                Employee employee = accrual.getEmployee();
//                Amount setPrice = settingsRepository.findByRank(employee.getRank());
//
////                LocalDate now = LocalDate.now();
//                LocalDate now = LocalDate.parse(A);
//                int currentYear = now.getYear();
//                YearMonth nowMonth = YearMonth.from(now);
//
//                LocalDate startDate = accrual.getStartDate() != null ? accrual.getStartDate() : now;
//                YearMonth startMonth = YearMonth.from(startDate);
//
//                LocalDate endDate = accrual.getEndDate() != null ? accrual.getEndDate() : now;
//                int retirementYear = endDate.getYear();
//                YearMonth endMonth = YearMonth.from(endDate);
//
//                long months = ChronoUnit.MONTHS.between(startMonth, endMonth);
//                int state = 0; //지급 불가능 - 기본값
//                if(months > 12){
//                    if(retirementYear < currentYear){
//                        state = 2; // 지급 완료
//                    } else {
//                        state = 1; // 지급 가능
//                    }
//                }
//                accrual.setState(state);
//
//            if (!accrual.getEmployee().getEmployeeType().equals(Employee.EmployeeType.CONTRACT)){
//                long result = 0;
//                // 결과 저장
//                // 총 적립금은 한 번만 설정
//                if (accrual.getEndDate() == null) {
//                    // 퇴직일이 없는 경우
//                    if (accrual.getTotalAmount() == null) {
//                      // 최초 계산: 현재 월까지 계산
//                            result = ( months +1 ) * setPrice.getMonthlyAmount();
//                            monthTotal += result;
//                    } else {
//                        // 누적 계산: 매월 추가 적립
//                        result = accrual.getTotalAmount() + setPrice.getMonthlyAmount();
//                        monthTotal += setPrice.getMonthlyAmount();
//                    }
//                } else {
//                    // 퇴직일이 있는 경우
//                    long totalMonths = 0;
//                    if (accrual.getTotalAmount() == null) {
//                        if (endMonth.isAfter(nowMonth) && !startMonth.equals(nowMonth)) {
//                            totalMonths = ChronoUnit.MONTHS.between(startMonth, nowMonth) + 1;
//                        }
//                        // 퇴직일이 현재 월 이전인 경우: 시작일부터 퇴직 월까지 계산
//                        else if (endMonth.isBefore(nowMonth)) {
//                            totalMonths = ChronoUnit.MONTHS.between(startMonth, endMonth) + 1;
//                        }
//                        else if (endMonth.equals(nowMonth)) {
//                            totalMonths = ChronoUnit.MONTHS.between(startMonth, nowMonth) + 1;
//                        }
//
//                        result = totalMonths * setPrice.getMonthlyAmount();
//                        monthTotal += result; // 누적 계산
//
//                    } else {
//                        if (nowMonth.isAfter(endMonth) || nowMonth.equals(endMonth)) {
//                            // 퇴직 전달 이후라면 적립 중단
//                            result = accrual.getTotalAmount(); // 기존 누적 금액 유지
//                        } else {
//                            // 퇴직 전달 이전이라면 적립 계속
//                            result = accrual.getTotalAmount() + setPrice.getMonthlyAmount();
//                            monthTotal += setPrice.getMonthlyAmount(); // 이번 달 적립액만 더함
//                        }
//                    }
//                }
//                accrual.setTotalAmount(result);  // 총 적립금은 한 번만 설정
//                accrualRepository.save(accrual);
//            }
//        }
//        monthLog.setMonthlyTotal(monthTotal);
//        monthLog.setSaveDate(LocalDate.now());
//        monthLogRepository.save(monthLog);
        List<Accrual> accrualList = accrualRepository.findAll();
        MonthLog monthLog = new MonthLog();
        long monthTotal = 0;

        for (Accrual accrual : accrualList) {
            Employee employee = accrual.getEmployee();
            Amount setPrice = settingsRepository.findByRank(employee.getRank());

            LocalDate now = LocalDate.parse(A);
            YearMonth nowMonth = YearMonth.from(now);

            LocalDate startDate = accrual.getStartDate() != null ? accrual.getStartDate() : now;
            YearMonth startMonth = YearMonth.from(startDate);

            LocalDate endDate = accrual.getEndDate() != null ? accrual.getEndDate() : now;
            YearMonth endMonth = YearMonth.from(endDate);

            long months = ChronoUnit.MONTHS.between(startMonth, endMonth);
            int state = (months > 12) ? (endDate.getYear() < now.getYear() ? 2 : 1) : 0;
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
        monthLog.setSaveDate(LocalDate.now());
        monthLogRepository.save(monthLog);
    }
}
