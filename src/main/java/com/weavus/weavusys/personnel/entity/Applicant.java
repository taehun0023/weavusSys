package com.weavus.weavusys.personnel.entity;

import com.weavus.weavusys.enums.AdmissionStatus;
import com.weavus.weavusys.enums.OfferStatus;
import com.weavus.weavusys.enums.VisaStatus;
import com.weavus.weavusys.personnel.dto.ApplicantDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

    @Entity
    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public class Applicant {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String name;  //이름(영어로)
        private LocalDate joiningDate; //입사예정일
        @Enumerated(EnumType.ORDINAL)
        private AdmissionStatus admissionStatus; // 합격 여부
        @Enumerated(EnumType.ORDINAL)
        private OfferStatus offerStatus; // 내정 상태
        private LocalDate visaApplicationDate; // 비자 신청일
        @Enumerated(EnumType.ORDINAL)
        private VisaStatus visaStatus; // 비자 상태

        @ManyToOne
        @JoinColumn(name = "institution_id", nullable = false)
        private Institution institution; // 소속 교육기관 조인

        public static Applicant fromDTO(ApplicantDTO applicantDTO, Institution institution) {
            Applicant applicant = new Applicant();
            applicant.setName(applicantDTO.getName());
            applicant.setJoiningDate(applicantDTO.getJoiningDate());
            applicant.setAdmissionStatus(AdmissionStatus.fromValue(applicantDTO.getAdmissionStatus()));
            applicant.setOfferStatus(OfferStatus.fromValue(applicantDTO.getOfferStatus()));
            applicant.setVisaStatus(VisaStatus.fromValue(applicantDTO.getVisaStatus()));
            applicant.setInstitution(institution);
            return applicant;

        }
    }
