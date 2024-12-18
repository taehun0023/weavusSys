package com.weavusys.hrd.repo;

import com.weavusys.hrd.entity.MonthLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthLogRepository extends JpaRepository<MonthLog, Long> {
}
