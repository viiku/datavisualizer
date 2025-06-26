package com.viiku.datavisualizer.repository;

import com.viiku.datavisualizer.model.entities.FileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadEntity, UUID> {
//    FileUploadEntity findById(UUID id);
}
