package com.weavus.weavusys.personnel.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

    @Entity
    @Data
    @NoArgsConstructor
    public class Institution {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private String contactInfo;
        @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true)
        @ToString.Exclude // 순환 참조 방지
        private List<Applicant> applicants;
        @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true)
        @ToString.Exclude
        private List<Schedule> schedules;

    }
