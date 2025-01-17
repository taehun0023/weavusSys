package com.weavus.weavusys.personnel.entity;

import com.weavus.weavusys.enums.ScheduleType;
import com.weavus.weavusys.personnel.dto.ScheduleDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String scheduleInfo;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    //어떤 스케줄인지 이넘으로 만들기 예 - 면접, 기업, 교육, 이벤트, 기타, 회계
    private ScheduleType scheduleType;

    //어느 기관의 스케쥴인지 조인
    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;

    public static Schedule fromSchedule(ScheduleDTO scheduleDTO, Institution institution) {
        Schedule schedule = new Schedule();
        schedule.setName(scheduleDTO.getName());
        schedule.setScheduleInfo(scheduleDTO.getScheduleInfo());
        schedule.setStartDateTime(scheduleDTO.getStartDateTime());
        schedule.setEndDateTime(scheduleDTO.getEndDateTime());
        schedule.setScheduleType(ScheduleType.fromValue(scheduleDTO.getScheduleType()));
        schedule.setInstitution(institution);
        return schedule;
    }
}
