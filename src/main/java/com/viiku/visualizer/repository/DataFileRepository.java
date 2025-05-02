package com.viiku.visualizer.repository;

import com.viiku.visualizer.model.entity.DataFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataFileRepository extends JpaRepository<DataFile, Long> {

}
