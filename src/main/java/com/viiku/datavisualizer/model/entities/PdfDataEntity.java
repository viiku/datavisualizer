package com.viiku.datavisualizer.model.entities;

import com.viiku.datavisualizer.common.model.entity.BaseEntity;
import com.viiku.datavisualizer.model.enums.FileUploadStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PdfDataEntity extends BaseEntity {


    @Id
    @GeneratedValue
    private UUID id;

    private String fileName;
    private String fileType;

    @Column(columnDefinition = "TEXT") // large JSON
    private String jsonData;

    private FileUploadStatus status;
}
