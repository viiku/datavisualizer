package com.viiku.datavisualizer.util.builder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viiku.datavisualizer.model.dtos.FileInfoDto;
import com.viiku.datavisualizer.model.dtos.ParsedFileResult;
import com.viiku.datavisualizer.model.entities.FileUploadEntity;
import com.viiku.datavisualizer.model.enums.files.FileStatus;
import com.viiku.datavisualizer.model.enums.files.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadBuilder {

    public FileUploadEntity buildFileUploadEntities(
            UUID fileId,
            MultipartFile file,
            ParsedFileResult parseResult) {


        FileInfoDto fileInfo = getFileInfo(file);
        List<String> metrics = getFileMetrics(parseResult);
        FileStatus status = FileStatus.UPLOADED;

        return FileUploadEntity.builder()
                .id(fileId)
                .fileName(fileInfo.getFileName())
                .fileSize(fileInfo.getFileSize())
                .fileType(fileInfo.getFileType())
                .status(status)
                .columnHeaders(metrics)
                .totalRows(getTotalRows(parseResult))
                .totalColumns(getTotalColumns(parseResult))
                .build();
    }

    private FileInfoDto getFileInfo(MultipartFile file) {
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("unknown");
        Long fileSize = file.getSize();
        String extension = FilenameUtils.getExtension(fileName);
        FileType fileType;

        switch (extension.toLowerCase()) {
            case "json" -> fileType = FileType.JSON;
            case "txt" , "text" -> fileType = FileType.TEXT;
            case "csv" -> fileType = FileType.CSV;
            case "pdf" -> fileType = FileType.PDF;
            case "xls", "xlsx" -> fileType = FileType.EXCEL;
            default -> fileType = FileType.UNKNOWN;
        }


        return FileInfoDto.builder()
                .fileName(fileName)
                .fileSize(fileSize)
                .fileType(fileType)
                .build();
    }

    /**
     * Step 3: Enhanced schema detection and metrics analysis
     */
    private List<String> getFileMetrics(ParsedFileResult parseResult) {
        List<String> detectedMetrics = new ArrayList<>(parseResult.getMetrics());

        // Add schema-based metrics
        if (parseResult.getJsonData() != null) {
            try {
                // Analyze JSON structure for additional metrics
                Map<String, Object> schemaInfo = analyzeDataStructure(parseResult.getJsonData());
                schemaInfo.forEach((key, value) ->
                        detectedMetrics.add(key + ": " + value.toString())
                );
            } catch (Exception e) {
                log.warn("Failed to analyze data structure for additional metrics: {}", e.getMessage());
            }
        }

        return detectedMetrics;
    }

    private Map<String, Object> analyzeDataStructure(String jsonData) {
        Map<String, Object> analysis = new HashMap<>();

        if (jsonData != null && !jsonData.trim().isEmpty()) {
            analysis.put("dataSize", jsonData.length());
            analysis.put("estimatedRecords", estimateRecordCount(jsonData));
            analysis.put("hasStructuredData", jsonData.contains("{") && jsonData.contains("}"));
        }

        return analysis;
    }

    private int extractRecordCount(String jsonData) {
        // Simple record count estimation
        if (jsonData == null) return 0;
        return jsonData.split("\\{").length - 1; // Rough estimate
    }

    private int estimateRecordCount(String jsonData) {
        return extractRecordCount(jsonData);
    }

    private int getTotalRows(ParsedFileResult parseResult) {
        try {
            return new ObjectMapper().readValue(
                    parseResult.getJsonData(),
                    new TypeReference<List<Map<String, String>>>() {}
            ).size();
        } catch (Exception e) {
            log.warn("Could not extract totalRows from JSON: {}", e.getMessage());
            return 0;
        }
    }

    private int getTotalColumns(ParsedFileResult parseResult) {
        return parseResult.getMetrics() != null ? parseResult.getMetrics().size() : 0;
    }

}
