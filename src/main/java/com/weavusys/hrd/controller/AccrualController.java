package com.weavusys.hrd.controller;

import com.weavusys.hrd.entity.Accrual;
import com.weavusys.hrd.entity.Amount;
import com.weavusys.hrd.service.AccrualService;
import com.weavusys.hrd.calCulBatch.BatchAccrualService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class AccrualController {

    private final AccrualService accrualService;
    private final BatchAccrualService batchAccrualService;

    @GetMapping("/accrual")
    public List<Accrual> getAllEmployees() {
        List<Accrual> accrualList = accrualService.findAll();
        return accrualList;
    }

    @GetMapping("/{id}/accrual")
    public Accrual getAccrual(@PathVariable String id) {
        Accrual accrual = accrualService.findByEmployeeId(id).orElseThrow();
        return accrual;
    }

    @GetMapping("/weavus12345/admintesturl")
    public void Test(@RequestParam String A) {
        batchAccrualService.calculateTotalAccrual(A);
    }

    @GetMapping("/admin/setting")
    public List<Amount> GetAdminSetting() {
        List<Amount> amountList = accrualService.findAllAmount();
        return amountList;
    }

    @PutMapping("/admin/setting")
    public ResponseEntity<String> updateAmounts(@RequestBody List<Amount> amounts) {
        try{
            for (Amount amount : amounts) {
                accrualService.updateAmount(amount); // 각 항목을 처리
            }
            return ResponseEntity.ok("수정 완료");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body("다시 확인해주세요: " + e.getMessage());
        }
    }
}