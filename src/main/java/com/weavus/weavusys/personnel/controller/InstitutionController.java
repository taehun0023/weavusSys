package com.weavus.weavusys.personnel.controller;

import com.weavus.weavusys.personnel.dto.InstitutionDTO;
import com.weavus.weavusys.personnel.dto.InstitutionDetailsDTO;
import com.weavus.weavusys.personnel.dto.ScheduleDTO;
import com.weavus.weavusys.personnel.entity.Institution;
import com.weavus.weavusys.personnel.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
public class InstitutionController {
    private final InstitutionService institutionService;

    @GetMapping("/institution/list")
    public List<InstitutionDTO> getAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @GetMapping("/institution/{id}")
    public InstitutionDetailsDTO getInstitutionDetails(@PathVariable Long id) {
        return institutionService.getInstitutionDetails(id);
    }
    //기관 정보를 저장하는 메소드
    @PostMapping("/institution/add")
    public Institution addInstitution(@RequestBody InstitutionDTO institutionDTO) {
        return institutionService.addInstitution(institutionDTO);
    }

    //기관 정보 수정 메소드
    @PutMapping("/institution/{id}")
    public ResponseEntity updateInstitution(@PathVariable Long id, @RequestBody InstitutionDTO institutionDTO) {
        return institutionService.updateInstitution(id, institutionDTO);
    }

    //기관 스케줄 작성 메소드 addSchedule사용
    @PostMapping("/institution/{id}/schedule/add")
    public ResponseEntity addSchedule(@PathVariable Long id, @RequestBody ScheduleDTO scheduleDTO) {
        return institutionService.addSchedule(id, scheduleDTO);
    }
    //기관 스케줄 수정 메소드 updateSchedule사용
    @PutMapping("/institution/{id}/schedule/{scheduleId}")
    public ResponseEntity updateSchedule(@PathVariable Long id, @PathVariable Long scheduleId, @RequestBody ScheduleDTO scheduleDTO) {
        return institutionService.updateSchedule(scheduleId, scheduleDTO);
    }

    //기관 스케줄 삭제 메소드 deleteSchedule사용
    @DeleteMapping("/institution/{id}/schedule/{scheduleId}")
    public ResponseEntity deleteSchedule(@PathVariable Long id, @PathVariable Long scheduleId) {
        return institutionService.deleteSchedule(scheduleId);
    }
}
