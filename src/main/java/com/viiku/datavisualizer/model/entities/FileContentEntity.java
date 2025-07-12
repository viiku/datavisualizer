package com.viiku.datavisualizer.model.entities;

import com.viiku.datavisualizer.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

// File content table - stores actual data from the file
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "file_contents",
        indexes = {
        @Index(name = "idx_file_row", columnList = "file_upload_id, rowIndex"),
        @Index(name = "idx_file_content_search", columnList = "file_upload_id, searchableContent")
})
public class FileContentEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    // Foreign key to file metadata
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_upload_id", nullable = false)
    private FileUploadEntity fileUpload;

    @Column(nullable = false)
    private Integer rowIndex;

    // Store the entire row as JSON for flexibility
    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String rowDataJson;

    // Optional: Store concatenated row data for search
    @Column(length = 1000)
    private String searchableContent;

    // Optional: Store row hash for duplicate detection
    @Column(length = 64)
    private String rowHash;

}
