package com.viiku.datavisualizer.repository;

import com.viiku.datavisualizer.model.entities.ExcelDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelDataRepository extends JpaRepository<ExcelDataEntity, Long> {
}
