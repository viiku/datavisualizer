//package com.viiku.datavisualizer.model.entities;
//
//import com.viiku.datavisualizer.common.model.entity.BaseEntity;
//import com.viiku.datavisualizer.model.enums.files.FileStatus;
//import jakarta.persistence.*;
//import lombok.*;
//import org.apache.pdfbox.util.filetypedetector.FileType;
//
//import java.util.List;
//import java.util.UUID;
//
//// Updated FileUploadEntity with optional dataset relationship
//@Entity
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "file_uploads_with_dataset")
//public class FileUploadWithDatasetEntity extends BaseEntity {
//
//    @Id
//    @GeneratedValue
//    private UUID id;
//
//    // Optional: Link to dataset if files are grouped
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "dataset_id")
//    private DatasetEntity dataset;
//
//    @Column(nullable = false, length = 255)
//    private String fileName;
//
//    @Column(nullable = false)
//    private Long fileSize;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private FileType fileType;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private FileStatus status;
//
//    @Column(length = 500)
//    private String filePath;
//
//    @ElementCollection
//    @CollectionTable(name = "file_column_headers", joinColumns = @JoinColumn(name = "file_id"))
//    @Column(name = "column_name")
//    private List<String> columnHeaders;
//
//    @Column
//    private Integer totalRows;
//
//    @Column
//    private Integer totalColumns;
//
//    @Lob
//    @Column(columnDefinition = "TEXT")
//    private String processingMetadata;
//
//    // Relationship to file content
//    @OneToMany(mappedBy = "fileUpload", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<FileContentEntity> fileContents;
//}
