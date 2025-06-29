package com.viiku.datavisualizer.repository;

import com.viiku.datavisualizer.model.entities.FileUploadEntity;
import com.viiku.datavisualizer.model.enums.files.FileStatus;
import com.viiku.datavisualizer.model.enums.files.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadEntity, UUID> {

//    Page<FileUploadEntity> findByStatusAndType(FileStatus status, FileType fileType, Pageable pageable);

//    Optional <FileUploadEntity> findById(UUID id);
}
