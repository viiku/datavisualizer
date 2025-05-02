package com.viiku.visualizer.model.dtos.payload.response;

import com.viiku.visualizer.model.enums.FileStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private String uploadId;
    private FileStatus status;
    private String message;
}
