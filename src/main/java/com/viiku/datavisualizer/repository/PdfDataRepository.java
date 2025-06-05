package com.viiku.datavisualizer.repository;

import com.viiku.datavisualizer.model.entities.PdfDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfDataRepository extends JpaRepository<PdfDataEntity, Long> {
}
