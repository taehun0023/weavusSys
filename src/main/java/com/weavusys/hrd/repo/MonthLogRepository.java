package com.weavusys.hrd.repo;

import com.weavusys.hrd.entity.MonthLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MonthLogRepository extends JpaRepository<MonthLog, Long> {
    @Query("SELECT m FROM MonthLog m WHERE FUNCTION('YEAR', m.saveDate) = :year")
    List<MonthLog> findBySaveDateYear(@Param("year") int year);}
