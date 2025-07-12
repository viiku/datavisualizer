package com.viiku.datavisualizer.repository;

import com.viiku.datavisualizer.model.entities.FileContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileContentRepository extends JpaRepository<FileContentEntity, UUID> {
}
