package com.weavus.weavusys.personnel.entity;

import com.weavus.weavusys.enums.AdmissionStatus;
import com.weavus.weavusys.enums.Gender;
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
        private String name;  //이름(영어 or 가타카나)
        @Column(nullable = false)
        @Enumerated(EnumType.ORDINAL)
        private Gender gender; //성별
        private String email; //이메일
        @Column(nullable = false)
        private LocalDate birthDate; //생년월일
        private String phoneNumber; //전화번호
        private LocalDate joiningDate; //입사예정일
        @Enumerated(EnumType.ORDINAL)
        @Column(nullable = false)
        private AdmissionStatus admissionStatus; // 전형 진행상태
        private LocalDate visaApplicationDate; // 비자 신청일
        @Enumerated(EnumType.ORDINAL)
        @Column(nullable = false)
        private VisaStatus visaStatus; // 비자 상태

        private LocalDate statusDate; // 상태 변경일

        @ManyToOne
        @JoinColumn(name = "institution_id", nullable = false)
        private Institution institution; // 소속 교육기관 조인

        //이력서 파일 저장
        @Lob
        private byte[] resume1;  // 첫 번째 이력서
        private String resumeFileName1;  // 첫 번째 이력서 파일 이름

        @Lob
        private byte[] resume2;  // 두 번째 이력서
        private String resumeFileName2;  // 두 번째 이력서 파일 이름

        @Lob
        private byte[] resume3;  // 세 번째 이력서
        private String resumeFileName3;  // 세 번째 이력서 파일 이름

        public static Applicant fromDTO(ApplicantDTO applicantDTO, Institution institution) {
            Applicant applicant = new Applicant();
            applicant.setName(applicantDTO.getName());
            applicant.setGender(Gender.valueOf(applicantDTO.getGender()));
            applicant.setEmail(applicantDTO.getEmail());
            applicant.setBirthDate(applicantDTO.getBirthDate());
            applicant.setPhoneNumber(applicantDTO.getPhoneNumber());
            applicant.setJoiningDate(applicantDTO.getJoiningDate());
            applicant.setAdmissionStatus(AdmissionStatus.fromValue(applicantDTO.getAdmissionStatus()));
            applicant.setVisaStatus(VisaStatus.fromValue(applicantDTO.getVisaStatus()));
            applicant.setVisaApplicationDate(applicantDTO.getVisaApplicationDate());
            applicant.setInstitution(institution);
            applicant.setResume1(applicantDTO.getResume1());
            applicant.setResumeFileName1(applicantDTO.getResumeFileName1());
            applicant.setResume2(applicantDTO.getResume2());
            applicant.setResumeFileName2(applicantDTO.getResumeFileName2());
            applicant.setResume3(applicantDTO.getResume3());
            applicant.setResumeFileName3(applicantDTO.getResumeFileName3());

            return applicant;

        }
    }
