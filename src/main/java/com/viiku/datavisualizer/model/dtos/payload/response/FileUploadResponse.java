package com.viiku.datavisualizer.model.dtos.payload.response;

import com.viiku.datavisualizer.model.enums.FileUploadStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private UUID fileId;
    private FileUploadStatus status;
    private String message;
}
