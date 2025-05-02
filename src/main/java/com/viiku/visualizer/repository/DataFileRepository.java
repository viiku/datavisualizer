package com.viiku.visualizer.repository;

import com.viiku.visualizer.model.entities.FileDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DataFileRepository extends JpaRepository<FileDataEntity, UUID> {

}
