package com.viiku.datavisualizer.service.impl;

import com.viiku.datavisualizer.common.exception.FileParsingException;
import com.viiku.datavisualizer.common.exception.FileUploadException;
import com.viiku.datavisualizer.model.dtos.FileProcessingDto;
import com.viiku.datavisualizer.model.dtos.ParsedFileResult;
import com.viiku.datavisualizer.model.entities.FileContentEntity;
import com.viiku.datavisualizer.model.payload.response.FileListResponse;
import com.viiku.datavisualizer.model.payload.response.FileStatusResponse;
import com.viiku.datavisualizer.model.payload.response.FileUploadResponse;
import com.viiku.datavisualizer.model.entities.FileUploadEntity;
import com.viiku.datavisualizer.model.enums.files.FileType;
import com.viiku.datavisualizer.model.enums.files.FileStatus;
import com.viiku.datavisualizer.model.mapper.FileUploadMapper;
import com.viiku.datavisualizer.repository.FileUploadRepository;
import com.viiku.datavisualizer.service.FileService;
import com.viiku.datavisualizer.util.builder.FileContentBuilder;
import com.viiku.datavisualizer.util.parser.FileParserStrategy;
import com.viiku.datavisualizer.util.builder.FileUploadBuilder;
import lombok.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Docs for different services
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final String TEMP_STORAGE_PATH = "temp/uploads/";
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final List<FileParserStrategy> parserStrategies;
    private final FileUploadRepository fileUploadRepository;
    private final FileUploadMapper fileUploadMapper;
    private final FileUploadBuilder fileUploadBuilder;
    private final FileContentBuilder fileContentBuilder;

    @Override
    public FileUploadResponse uploadAndProcessFile(MultipartFile file) {

        try {
            return parseAndSave(file);
        } catch (FileParsingException e) {
            log.error("File parsing failed: {}", e.getMessage());
            throw new FileUploadException("Parsing failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during file upload: {}", e.getMessage(), e);
            throw new FileUploadException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public FileStatusResponse getFileStatus(UUID fileId) {
        FileUploadEntity entity = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new FileUploadException("File not found with id: " + fileId));

        return FileStatusResponse.builder()
                .fileId(fileId)
                .fileStatus(entity.getStatus())
                .progress(10)
                .processedRecords(1000)
                .totalRecords(12000)
                .message("Getting entity from DB")
                .build();
    }

    /**
     * Delete file
     */
    @Override
    public void deleteFile(UUID fileId) {
        fileUploadRepository.deleteById(fileId);
        log.info("File deleted successfully: {}", fileId);
    }

    @Override
    public Object getFilePreview(UUID fileId, int rows) {
        return null;
    }

    @Override
    public FileUploadResponse retryProcessing(UUID fileId) {
        return null;
    }

    @Override
    public FileListResponse getFileList(Pageable pageable, FileStatus status, FileType fileType) {
//        Page<FileUploadEntity> page = fileUploadRepository.findByStatusAndType(status, fileType, pageable);
//        List<FileUploadResponse> uploads = page.getContent().stream()
//                .map(fileUploadMapper::mapToTarget)
//                .toList();
//
//        return FileListResponse.builder()
//                .files(uploads)
//                .totalPages((int) page.getTotalElements())
//                .currentPage(page.getNumber())
//                .pageSize(page.getSize())
//                .build();
        return null;
    }

    private FileUploadResponse parseAndSave(MultipartFile file) throws Exception {
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("unknown");
        String extension = FilenameUtils.getExtension(fileName);
        UUID fileId = UUID.randomUUID();

        log.info("Starting file processing pipeline for {} (ID: {})", fileName, fileId);
        String tempFilePath = saveToTemporaryStorage(file, fileId);

        try {
            FileParserStrategy fileParser = findParserStrategy(extension);
            ParsedFileResult parseResult = fileParser.parse(file);

            FileUploadEntity fileUploadEntity = fileUploadBuilder.buildFileUploadEntities(fileId, file, parseResult);
            List<FileContentEntity> contents = fileContentBuilder.buildFileContentEntities(parseResult, fileUploadEntity);
            fileUploadEntity.setFileContents(contents);
            FileUploadEntity savedEntity = fileUploadRepository.save(fileUploadEntity);

            startAsyncProcessing(fileId, tempFilePath, parseResult, savedEntity);

            FileUploadResponse response = fileUploadMapper.mapToTarget(savedEntity);

            log.info("File upload initiated successfully. FileId: {}, Status: PROCESSING", fileId);
            return response;
        } catch (Exception e) {
            cleanupTempFile(tempFilePath);
            throw e;
        }
    }

    /**
     * Step 1: Save file to temporary storage with unique naming
     */
    private String saveToTemporaryStorage(MultipartFile file, UUID fileId) throws IOException {

        // Create temp directory if it doesn't exist
        Path tempDir = Paths.get(TEMP_STORAGE_PATH);
        if (!Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }

        // Generate unique temp file name
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String tempFileName = String.format("%s_%d.%s",
                fileId.toString(), System.currentTimeMillis(), extension);

        Path tempFilePath = tempDir.resolve(tempFileName);

        // Save file to temporary storage
        Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

        log.debug("File saved to temporary storage: {}", tempFilePath);
        return tempFilePath.toString();
    }

    /**
     * Step 2: Find appropriate parser strategy
     */
    private FileParserStrategy findParserStrategy(String extension) throws FileParsingException {
        return parserStrategies.stream()
                .filter(parser -> parser.supports(extension))
                .findFirst()
                .orElseThrow(() -> new FileParsingException("Unsupported file type: " + extension));
    }

    /**
     * Step 4: Start asynchronous processing
     */
    @Async
    public CompletableFuture<Void> startAsyncProcessing(
            UUID fileId, String tempFilePath, ParsedFileResult parseResult, FileUploadEntity entity) {

        return CompletableFuture.runAsync(() -> {
            try {
                log.info("Starting async processing for file: {}", fileId);

                // Update status to processing
                updateFileStatus(fileId, FileStatus.PROCESSING, "Processing started", null);

                // Perform intensive processing operations
                FileProcessingDto result = performDataProcessing(parseResult, entity);

                // Update entity with processing results
                updateFileWithProcessingResults(fileId, result);

                // Update final status
                updateFileStatus(fileId, FileStatus.COMPLETED, "Processing completed successfully", result);

                log.info("Async processing completed successfully for file: {}", fileId);

            } catch (Exception e) {
                log.error("Async processing failed for file {}: {}", fileId, e.getMessage(), e);
                updateFileStatus(fileId, FileStatus.FAILED, "Processing failed: " + e.getMessage(), null);
            } finally {
                // Always cleanup temp file
                cleanupTempFile(tempFilePath);
            }
        });
    }

    /**
     * Perform data processing operations
     */
    private FileProcessingDto performDataProcessing(ParsedFileResult parseResult, FileUploadEntity entity) {
        FileProcessingDto result = new FileProcessingDto();

        try {
            // Simulate processing steps
            String jsonData = parseResult.getJsonData();

            // Step 1: Data validation
            validateProcessedData(jsonData);
            result.addStep("Data validation completed");

            // Step 2: Data transformation
            String transformedData = transformData(jsonData);
            result.setTransformedData(transformedData);
            result.addStep("Data transformation completed");

            // Step 3: Generate statistics
            Map<String, Object> statistics = generateDataStatistics(jsonData);
            result.setStatistics(statistics);
            result.addStep("Statistics generation completed");

            // Step 4: Create indexes (if needed)
            createDataIndexes(entity.getId(), transformedData);
            result.addStep("Data indexing completed");

            result.setStatus("SUCCESS");
//            result.setProcessedRecords(extractRecordCount(jsonData));
            result.setProcessedRecords(29);

        } catch (Exception e) {
            result.setStatus("FAILED");
            result.addError("Processing failed: " + e.getMessage());
            throw new RuntimeException("Data processing failed", e);
        }

        return result;
    }

    /**
     * Update file status in database
     */
    private void updateFileStatus(UUID fileId, FileStatus status, String message, FileProcessingDto result) {
        try {
            Optional<FileUploadEntity> entityOpt = fileUploadRepository.findById(fileId);
            if (entityOpt.isPresent()) {
                FileUploadEntity entity = entityOpt.get();
                entity.setStatus(status);
//                entity.setLastUpdated(LocalDateTime.now());

                // Update metrics with processing results
                if (result != null && result.getStatistics() != null) {
                    List<String> updatedMetrics = new ArrayList<>(entity.getColumnHeaders());
                    result.getStatistics().forEach((key, value) ->
                            updatedMetrics.add(key + ": " + value.toString())
                    );
                    entity.setColumnHeaders(updatedMetrics);
                }

                fileUploadRepository.save(entity);
                log.debug("Updated file status: {} -> {}", fileId, status);
            }
        } catch (Exception e) {
            log.error("Failed to update file status for {}: {}", fileId, e.getMessage());
        }
    }

    private void validateProcessedData(String jsonData) {
        if (jsonData == null || jsonData.trim().isEmpty()) {
            throw new RuntimeException("Processed data is empty");
        }
        // Add more validation logic as needed
    }

    private String transformData(String jsonData) {
        // Implement data transformation logic
        // This could include normalization, cleaning, etc.
        return jsonData; // Placeholder
    }

    private Map<String, Object> generateDataStatistics(String jsonData) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("recordCount", 29);
        stats.put("dataSize", jsonData.length());
        stats.put("processedAt", LocalDateTime.now());
        return stats;
    }

    private void createDataIndexes(UUID fileId, String data) {
        // Implement indexing logic for faster queries
        log.debug("Creating indexes for file: {}", fileId);
    }

    private void updateFileWithProcessingResults(UUID fileId, FileProcessingDto result) {
        // Update entity with final processing results
        Optional<FileUploadEntity> entityOpt = fileUploadRepository.findById(fileId);
        if (entityOpt.isPresent()) {
            FileUploadEntity entity = entityOpt.get();
//            if (result.getTransformedData() != null) {
//                entity.setJsonData(result.getTransformedData());
//            }
            fileUploadRepository.save(entity);
        }
    }

    private void cleanupTempFile(String tempFilePath) {
        try {
            if (tempFilePath != null) {
                Files.deleteIfExists(Paths.get(tempFilePath));
                log.debug("Cleaned up temporary file: {}", tempFilePath);
            }
        } catch (IOException e) {
            log.warn("Failed to cleanup temporary file {}: {}", tempFilePath, e.getMessage());
        }
    }
    
}