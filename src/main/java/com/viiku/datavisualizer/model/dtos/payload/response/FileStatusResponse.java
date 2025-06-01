package com.viiku.datavisualizer.model.dtos.payload.response;

import com.viiku.datavisualizer.model.enums.FileUploadStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileStatusResponse {

    private String uploadId;
    private FileUploadStatus fileUploadStatus;
    private String message;
}
