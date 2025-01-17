package com.weavus.weavusys.personnel.controller;


import com.weavus.weavusys.personnel.dto.ApplicantDTO;
import com.weavus.weavusys.personnel.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;

    @GetMapping("/applicant/list")
    public List<ApplicantDTO> getAllApplicants() {
        return applicantService.getAllApplicants();
    }

    @GetMapping("/applicant/{id}")
    public ApplicantDTO getInstitutionDetails(@PathVariable Long id) {
        return applicantService.getApplicantDetails(id);
    }

    @PostMapping("/applicant/add")
    public String addApplicant(@RequestBody ApplicantDTO applicantDTO) {
        return applicantService.addApplicant(applicantDTO);
    }

    @PutMapping("/applicant/{id}")
    public String updateApplicant(@PathVariable Long id, @RequestBody ApplicantDTO applicantDTO) {
        return applicantService.updateApplicant(id, applicantDTO);
    }

    //지원자 삭제
    @DeleteMapping("/applicant/{id}")
    public String deleteApplicant(@PathVariable Long id) {
        return applicantService.deleteApplicant(id);
    }

    //지원자 이력서 djqfhem
//    @PostMapping("/applicant/{id}/upload")
//    public String uploadResume(@PathVariable Long id, @RequestParam("resume") MultipartFile multipartFile) {
//        return applicantService.uploadResume(id, multipartFile);
//    }
//
//    //지원자 이력서 다운로드
//    @GetMapping("/applicant/{id}/download")
//    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) {
//        return applicantService.downloadResume(id);
//    }
}
