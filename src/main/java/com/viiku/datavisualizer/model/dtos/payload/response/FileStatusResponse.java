package com.viiku.datavisualizer.model.dtos.payload.response;

import com.viiku.datavisualizer.model.enums.FileStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileStatusResponse {

    private String uploadId;
    private FileStatus fileStatus;
    private String message;
}
