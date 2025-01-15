package com.weavus.weavusys.personnel.service;


import com.weavus.weavusys.personnel.dto.InstitutionDTO;
import com.weavus.weavusys.personnel.dto.InstitutionDetailsDTO;
import com.weavus.weavusys.personnel.dto.ScheduleDTO;
import com.weavus.weavusys.personnel.entity.Applicant;
import com.weavus.weavusys.personnel.entity.Institution;
import com.weavus.weavusys.personnel.entity.Schedule;
import com.weavus.weavusys.personnel.repository.InstitutionRepository;
import com.weavus.weavusys.personnel.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final ScheduleRepository scheduleRepository;

    public List<InstitutionDTO> getAllInstitutions() {
        return institutionRepository.findAll().stream()
                .map(InstitutionDTO::convertToDTO)
                .collect(Collectors.toList());
    }


    public InstitutionDetailsDTO getInstitutionDetails(Long id) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Institution with id " + id + " not found"));

        InstitutionDetailsDTO dto = new InstitutionDetailsDTO();
        dto.setName(institution.getName());
        dto.setContactInfo(institution.getContactInfo());
        dto.setApplicantNames(
                institution.getApplicants() != null
                        ? institution.getApplicants().stream().map(Applicant::getName).collect(Collectors.toList())
                        : List.of()
        );

        dto.setSchedules(
                scheduleRepository.findByInstitutionId(id) != null
                        ? scheduleRepository.findByInstitutionId(id).stream().map(ScheduleDTO::ToScheduleDTO).collect(Collectors.toList())
                        : List.of()
        );
        return dto;
    }

    public Institution addInstitution(InstitutionDTO institutionDTO) {
        if (institutionRepository.existsByName(institutionDTO.getName())) {
            throw new IllegalArgumentException("Institution with name " + institutionDTO.getName() + " already exists");
        }

        Institution institution = new Institution();
        institution.setName(institutionDTO.getName());
        institution.setContactInfo(institutionDTO.getContactInfo());

        return institutionRepository.save(institution);
    }

    public ResponseEntity updateInstitution(Long id, InstitutionDTO institutionDTO) {

        try{
            Institution institution = institutionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
            institution.setName(institutionDTO.getName());
            institution.setContactInfo(institutionDTO.getContactInfo());
            institutionRepository.save(institution);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            Map<String, String > error = new HashMap<>();
            error.put("error", "Invalid Request");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    //시업에 스케줄 추가
    public ResponseEntity addSchedule(Long institutionId, ScheduleDTO scheduleDTO) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
        scheduleDTO.setInstitutionId(institutionId);
        scheduleRepository.save(Schedule.fromSchedule(scheduleDTO, institution));
        return ResponseEntity.ok().build();
    }

    //스케줄 수정
    public ResponseEntity updateSchedule(Long scheduleId, ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        schedule.setName(scheduleDTO.getName());
        schedule.setScheduleInfo(scheduleDTO.getScheduleInfo());
        schedule.setStartDateTime(scheduleDTO.getStartDateTime());
        schedule.setEndDateTime(scheduleDTO.getEndDateTime());

        scheduleRepository.save(schedule);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        scheduleRepository.delete(schedule);
        return ResponseEntity.ok().build();
    }
}
