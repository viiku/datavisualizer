package com.viiku.datavisualizer.model.entities;

import com.viiku.datavisualizer.common.model.entity.BaseEntity;
import com.viiku.datavisualizer.model.enums.FileType;
import com.viiku.datavisualizer.model.enums.FileUploadStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_uploads")
public class FileUploadEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String jsonData;

    @ElementCollection
    private List<String> metrics;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileUploadStatus status;
}
