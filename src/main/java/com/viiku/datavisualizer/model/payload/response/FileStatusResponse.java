package com.viiku.datavisualizer.model.payload.response;

import com.viiku.datavisualizer.model.enums.FileStatus;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileStatusResponse {

    private UUID fileId;
    private FileStatus fileStatus;
    private int progress;
    private int processedRecords;
    private int totalRecords;
    private String message;
    private List<String> errors;
}
