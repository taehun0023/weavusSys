package com.weavus.weavusys.calcul.repo;

import com.weavus.weavusys.calcul.entity.Amount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Amount, Long> {
    Amount findByRank(Integer id);
}
