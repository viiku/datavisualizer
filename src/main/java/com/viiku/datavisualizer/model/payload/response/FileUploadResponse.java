package com.viiku.datavisualizer.model.payload.response;

import com.viiku.datavisualizer.model.enums.FileType;
import com.viiku.datavisualizer.model.enums.FileStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private UUID fileId;
    private UUID datasetId;
    private String fileName;
    private Long fileSize;
    private FileType fileType;
    private LocalDateTime uploadTimestamp;
    private FileStatus uploadStatus;
    private List<String> detectedMetrics;
    private String message;
}
