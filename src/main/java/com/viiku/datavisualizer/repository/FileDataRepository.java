package com.viiku.datavisualizer.repository;

import com.viiku.datavisualizer.model.entities.FileDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileDataRepository extends JpaRepository<FileDataEntity, UUID> {

}
