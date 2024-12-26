package com.weavusys.hrd.service;

import com.weavusys.hrd.entity.Accrual;
import com.weavusys.hrd.entity.Amount;
import com.weavusys.hrd.entity.Employee;
import com.weavusys.hrd.entity.MonthLog;
import com.weavusys.hrd.repo.AccrualRepository;
import com.weavusys.hrd.repo.EmployeeRepository;
import com.weavusys.hrd.repo.MonthLogRepository;
import com.weavusys.hrd.repo.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccrualRepository accrualRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final SettingsRepository settingsRepository;
    private final MonthLogRepository monthLogRepository;

    public List<Employee> findAll() {
        return employeeRepository.findByStatus(0);
    }

    public String save(Employee employee) {

        //직원 등록 시 초기 퇴직금 계산용 날짜 계산 변수
        LocalDate now = LocalDate.now();
        YearMonth nowMonth = YearMonth.from(now);

        LocalDate startDate = employee.getConversionDate() != null ? employee.getConversionDate() : now;
        YearMonth startMonth = YearMonth.from(startDate);

        LocalDate endDate = employee.getExitDate() != null ? employee.getExitDate() : now;
        YearMonth endMonth = YearMonth.from(endDate);

        long months = ChronoUnit.MONTHS.between(startMonth, endMonth);
        Amount setPrice = settingsRepository.findByRank(employee.getRank());
        MonthLog monthLog = new MonthLog();
        long monthTotal = 0;

        int state = (months > 12) ? (endDate.getYear() < now.getYear() ? 2 : 1) : 0;
        // month 12개월 이하 시 0 값을 state에 부여,
        // month 12개월 초과 시 now와 endDate를 비교하여 2, 1값 을 state에 부여

        Optional<Employee> employeeDto = employeeRepository.findById(employee.getId());
        if (employeeDto.isEmpty()){ //변수명 불명확하여 변경
            try {
                employee.setStatus(0);
                employeeRepository.save(employee);
                Accrual accrual = new Accrual();
                accrual.setEmployee(employee);
                accrual.setState(state);

                // 기존 정직원 초기 등록 적립금 계산
                if(employee.getEmployeeType().equals(Employee.EmployeeType.REGULAR)){

                    long resultFirst = 0; //직원 등록 시 적립금 최초 계산값 저장 변수
                    if(!startMonth.equals(endMonth)){
                        long totalMonths = endMonth.isAfter(nowMonth)
                                ? ChronoUnit.MONTHS.between(startMonth, nowMonth) + 1
                                : ChronoUnit.MONTHS.between(startMonth, endMonth) + 1;
                        resultFirst = totalMonths * setPrice.getMonthlyAmount();
                        monthTotal += resultFirst;
                    }
                    accrual.setTotalAmount(resultFirst);

                    // 초기 등록시 적립된 금액 로그 저장
                    monthLog.setMonthlyTotal(monthTotal);
                    monthLog.setSaveDate(now);
                    monthLogRepository.save(monthLog);
                }

                if (employee.getExitDate() != null){
                    accrual.setEndDate(employee.getExitDate());
                }

                accrual.setStartDate(startDate);
                accrualRepository.save(accrual);
                return "등록이 완료되었습니다."; //저장 성공 여부 바로 메세지 발송으로 변경
            } catch (Exception e){
                return "다시 한번 입력해 주세요.";
            }
        }
        return "아이디가 중복입니다.";
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
            employee.setStatus(1);
            employeeRepository.save(employee);
        });
        return optionalEmployee.isPresent();
    }
}
