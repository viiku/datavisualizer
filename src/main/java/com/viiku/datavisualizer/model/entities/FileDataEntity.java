package com.viiku.datavisualizer.model.entities;

import com.viiku.datavisualizer.common.model.entity.BaseEntity;
import com.viiku.datavisualizer.model.enums.FileStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDataEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String fileName;
    private String fileType;

    @Column(columnDefinition = "TEXT") // large JSON
    private String jsonData;

    private FileStatus status;

//    private String userId; // optional for multi-tenancy
//    private String tenantId; // optional for SaaS
}
