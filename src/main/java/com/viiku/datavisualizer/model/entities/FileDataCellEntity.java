//package com.viiku.datavisualizer.model.entities;
//
//import com.viiku.datavisualizer.common.model.entity.BaseEntity;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.UUID;
//
//// Alternative: More normalized approach with individual cells
//@Entity
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(
//        name = "file_data_cells",
//        indexes = {
//        @Index(name = "idx_file_row_col", columnList = "file_upload_id, rowIndex, columnIndex"),
//        @Index(name = "idx_file_column_name", columnList = "file_upload_id, columnName")
//})
//public class FileDataCellEntity extends BaseEntity {
//
//    @Id
//    @GeneratedValue
//    private UUID id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "file_upload_id", nullable = false)
//    private FileUploadEntity fileUpload;
//
//    @Column(nullable = false)
//    private Integer rowIndex;
//
//    @Column(nullable = false)
//    private Integer columnIndex;
//
//    @Column(length = 100)
//    private String columnName;
//
//    @Lob
//    @Column(columnDefinition = "TEXT")
//    private String cellValue;
//
//    // Optional: Store data type for better querying
//    @Column(length = 20)
//    private String dataType; // STRING, NUMBER, DATE, BOOLEAN, etc.
//
//}
