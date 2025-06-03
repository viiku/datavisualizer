package com.viiku.datavisualizer.repository;

import com.viiku.datavisualizer.model.entities.CsvDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CsvDataRepository extends JpaRepository<CsvDataEntity, UUID> {

}
