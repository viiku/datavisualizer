package com.viiku.datavisualizer.model.entities;

import com.viiku.datavisualizer.common.model.entity.BaseEntity;
import com.viiku.datavisualizer.model.enums.files.FileType;
import com.viiku.datavisualizer.model.enums.files.FileStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus status;

    // Store column headers as metadata
    @ElementCollection
    @CollectionTable(name = "file_column_headers", joinColumns = @JoinColumn(name = "file_id"))
    @Column(name = "column_name")
    private List<String> columnHeaders;

    // Store file processing metadata
    @Column
    private Integer totalRows;

    @Column
    private Integer totalColumns;

    // Optional: Store file summary/statistics as JSON
//    @Lob
//    @Column(columnDefinition = "TEXT")
//    private String processingMetadata;


//    @ManyToOne
//    @JoinColumn(name = "dataset_id")
//    private DatasetEntity dataset;

    @OneToMany(mappedBy = "fileUpload", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileContentEntity> fileContents;

//    @OneToMany(mappedBy = "fileUpload", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<FileDataCellEntity> dataCells;
}
