package com.weavus.weavusys.personnel.repository;

import com.weavus.weavusys.personnel.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByInstitutionId(Long id);
}
