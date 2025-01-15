package com.weavus.weavusys.personnel.dto;

import com.weavus.weavusys.personnel.entity.Applicant;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicantDTO {  //대소문자 확인하기
    private Long id;
    private String name;
    private LocalDate joiningDate;
    private int admissionStatus;
    private int offerStatus;
    private LocalDate visaApplicationDate;
    private int visaStatus;
    private Long institutionId;

    public static ApplicantDTO toDTO(Applicant applicant) {
        ApplicantDTO dto = new ApplicantDTO();
        dto.setId(applicant.getId());
        dto.setName(applicant.getName());
        dto.setJoiningDate(applicant.getJoiningDate());
        dto.setVisaApplicationDate(applicant.getVisaApplicationDate());
        dto.setAdmissionStatus(applicant.getAdmissionStatus().getValue());
        dto.setOfferStatus(applicant.getOfferStatus().getValue());
        dto.setVisaStatus(applicant.getVisaStatus().getValue());
        dto.setInstitutionId(applicant.getInstitution().getId());
        return dto;
    }
}
