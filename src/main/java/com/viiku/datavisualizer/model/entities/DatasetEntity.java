//package com.viiku.datavisualizer.model.entities;
//
//import com.viiku.datavisualizer.common.model.entity.BaseEntity;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//import java.util.UUID;
//
//// Optional: Dataset entity for grouping multiple files
//@Entity
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "datasets")
//public class DatasetEntity extends BaseEntity {
//
//    @Id
//    @GeneratedValue
//    private UUID id;
//
//    @Column(nullable = false, length = 255)
//    private String name;
//
//    @Column(length = 1000)
//    private String description;
//
//    @Column(length = 100)
//    private String createdBy;
//
//    // One dataset can contain multiple files
//    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL)
//    private List<FileUploadEntity> files;
//
//}
