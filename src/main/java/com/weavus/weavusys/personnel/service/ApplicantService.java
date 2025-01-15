package com.weavus.weavusys.personnel.service;

import com.weavus.weavusys.enums.AdmissionStatus;
import com.weavus.weavusys.enums.OfferStatus;
import com.weavus.weavusys.enums.VisaStatus;
import com.weavus.weavusys.personnel.dto.ApplicantDTO;
import com.weavus.weavusys.personnel.entity.Applicant;
import com.weavus.weavusys.personnel.entity.Institution;
import com.weavus.weavusys.personnel.repository.ApplicantRepository;
import com.weavus.weavusys.personnel.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final InstitutionRepository institutionRepository;

    public List<ApplicantDTO> getAllApplicants() {
        return applicantRepository.findAll().stream()
                .map(ApplicantDTO::toDTO)
                .collect(Collectors.toList());
    }

    public ApplicantDTO getApplicantDetails(Long id) {
        //개별적인 id를 가지고 유저 정보를 취득한다 dto변환은 toDTO사용
        Applicant applicant = applicantRepository.findById(id).orElseThrow();
        return ApplicantDTO.toDTO(applicant);
    }

    public String addApplicant(ApplicantDTO applicantDTO) {
        Institution institution = institutionRepository.findById(applicantDTO.getInstitutionId())
                .orElseThrow(
                () -> new IllegalArgumentException("Institution with id " + applicantDTO.getInstitutionId() + " not found")
        );

        applicantRepository.save(Applicant.fromDTO(applicantDTO, institution));
        return "Applicant added successfully";
    }

    public String updateApplicant(Long id, ApplicantDTO applicantDTO) {
        Applicant applicant = applicantRepository.findById(id).orElseThrow();
        applicant.setName(applicantDTO.getName());
        applicant.setJoiningDate(applicantDTO.getJoiningDate());
        applicant.setAdmissionStatus(AdmissionStatus.fromValue(applicantDTO.getAdmissionStatus()));
        applicant.setOfferStatus(OfferStatus.fromValue(applicantDTO.getOfferStatus()));
        applicant.setVisaStatus(VisaStatus.fromValue(applicantDTO.getVisaStatus()));
        applicant.setInstitution(institutionRepository.findById(applicantDTO.getInstitutionId()).orElseThrow());
        applicantRepository.save(applicant);
        return "Applicant updated successfully";
    }

    public String deleteApplicant(Long id) {
        applicantRepository.deleteById(id);
        return "Applicant deleted successfully";
    }
}
