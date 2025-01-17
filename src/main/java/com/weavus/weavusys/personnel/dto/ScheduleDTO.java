package com.weavus.weavusys.personnel.dto;

import com.weavus.weavusys.personnel.entity.Schedule;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDTO {

    private Long id;
    private String name;
    private String scheduleInfo;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long institutionId;
    private int scheduleType;

    public static ScheduleDTO ToScheduleDTO(Schedule schedule) {

        ScheduleDTO dto = new ScheduleDTO();
        dto.setName(schedule.getName());
        dto.setScheduleInfo(schedule.getScheduleInfo());
        dto.setStartDateTime(schedule.getStartDateTime());
        dto.setEndDateTime(schedule.getEndDateTime());
        dto.setInstitutionId(schedule.getInstitution().getId());
        dto.setScheduleType(schedule.getScheduleType().getValue());
        return dto;
    }
}
