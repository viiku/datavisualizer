package com.viiku.datavisualizer.util;

import com.viiku.datavisualizer.common.exception.InvalidFileTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * File validation utility
 */
@Component
@Slf4j
public class FileValidation {

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".csv", ".xls", ".xlsx", ".pdf"
    );

    public void validateUploadedFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new InvalidFileTypeException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileTypeException(
                    String.format("File size %d exceeds maximum allowed size %d",
                            file.getSize(), MAX_FILE_SIZE)
            );
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !hasValidExtension(filename)) {
            throw new InvalidFileTypeException(
                    "Invalid file type. Allowed types: " + String.join(", ", ALLOWED_EXTENSIONS)
            );
        }


        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new InvalidFileTypeException("Invalid content type: " + contentType);
        }
    }

    private boolean hasValidExtension(String filename) {
        return ALLOWED_EXTENSIONS.stream()
                .anyMatch(ext -> filename.toLowerCase().endsWith(ext));
    }

    private boolean isAllowedContentType(String contentType) {
        return Arrays.asList(
                "text/csv",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/pdf",
                "application/octet-stream" // Some browsers send this for CSV
        ).contains(contentType);
    }
}
