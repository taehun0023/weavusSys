package com.weavus.weavusys.personnel.repository;

import com.weavus.weavusys.personnel.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    boolean existsByName(String name);
}
