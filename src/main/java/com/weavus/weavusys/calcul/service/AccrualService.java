package com.weavus.weavusys.calcul.service;

import com.weavus.weavusys.calcul.repo.SettingsRepository;
import com.weavus.weavusys.calcul.entity.Accrual;
import com.weavus.weavusys.calcul.entity.Amount;
import com.weavus.weavusys.calcul.repo.AccrualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccrualService {

    private final AccrualRepository accrualRepository;
    private final SettingsRepository settingsRepository;

    public Optional<Accrual> findByEmployeeId(String id) {
        Optional<Accrual> accrual = Optional.ofNullable(accrualRepository.findByEmployeeId(id));
        return accrual;
    }

    public List<Accrual> findAll() {
        List<Accrual> accrualList = accrualRepository.findAll();
        return accrualList;
    }

    public void updateAmount(Amount amount) {
        Amount existingAmount = settingsRepository.findById(amount.getRank())
                .orElseThrow(() -> new RuntimeException("해당 ID를 찾을 수 없습니다."));
        // 값 업데이트
        existingAmount.setMonthlyAmount(amount.getMonthlyAmount());
        settingsRepository.save(existingAmount);
    }

    public List<Amount> findAllAmount() {
        return settingsRepository.findAll();
    }
}
