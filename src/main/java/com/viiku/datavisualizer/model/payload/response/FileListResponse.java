package com.viiku.datavisualizer.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * File list response DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileListResponse {

    private List<FileUploadResponse> files;
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}
