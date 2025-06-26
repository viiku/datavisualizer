package com.viiku.datavisualizer.service.impl;

import com.viiku.datavisualizer.common.exception.FileParsingException;
import com.viiku.datavisualizer.common.exception.FileUploadException;
import com.viiku.datavisualizer.model.dtos.FileInfoDto;
import com.viiku.datavisualizer.model.dtos.ParsedFileResult;
import com.viiku.datavisualizer.model.dtos.payload.response.FileStatusResponse;
import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
import com.viiku.datavisualizer.model.entities.FileUploadEntity;
import com.viiku.datavisualizer.model.enums.FileType;
import com.viiku.datavisualizer.model.enums.FileUploadStatus;
import com.viiku.datavisualizer.model.mapper.FileUploadMapper;
import com.viiku.datavisualizer.repository.FileUploadRepository;
import com.viiku.datavisualizer.service.FileService;
import com.viiku.datavisualizer.util.FileParserStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.viiku.datavisualizer.util.FileConstants.*;

/**
 * Docs for different services
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final List<FileParserStrategy> parserStrategies;
    private final FileUploadRepository fileUploadRepository;
    private final FileUploadMapper fileUploadMapper;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new FileUploadException("Uploaded file is empty.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals(CSV_CONTENT_TYPE) ||
                contentType.equals(PDF_CONTENT_TYPE) ||
                contentType.equals(EXCEL_CONTENT_TYPE))) {

            FileInfoDto fileInfo = getFileInfo(file);

            return FileUploadResponse.builder()
                    .fileId(null)
                    .fileName(fileInfo.getFileName())
                    .fileSize(fileInfo.getFileSize())
                    .fileType(FileType.UNKNOWN)
                    .uploadTimestamp(null)
                    .uploadStatus(FileUploadStatus.FAILED)
                    .message("Unsupported file type. Please upload CSV, Excel, or PDF.")
                    .detectedMetrics(null)
                    .build();
        }

        try {
            return parseAndSave(file);
        } catch (FileParsingException e) {
            throw new FileUploadException("Parsing failed: " + e.getMessage());
        } catch (Exception e) {
            throw new FileUploadException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public FileStatusResponse getFileStatus(UUID fileId) {
        FileUploadEntity entity = fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new FileUploadException("File not found with id: " + fileId));

        return FileStatusResponse.builder()
                .fileId(fileId)
                .fileUploadStatus(entity.getStatus())
                .progress(10)
                .processedRecords(1000)
                .totalRecords(12000)
                .message("Getting entity from DB")
                .build();
    }

    /**
     * Fully implemented method to parse and store file data.
     * It finds the appropriate parser, persists the parsed data in the related repository,
     * and creates a FileUploadEntity record.
     */
    private FileUploadResponse parseAndSave(MultipartFile file) throws Exception {
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("unknown");
        String extension = FilenameUtils.getExtension(fileName);

        FileParserStrategy fileParser = parserStrategies.stream()
                .filter(p -> p.supports(extension))
                .findFirst()
                .orElseThrow(() -> new FileParsingException("Unsupported file type: " + extension));

        UUID fileId = UUID.randomUUID();
        ParsedFileResult result = fileParser.parse(file);
        String jsonData = result.getJsonData();
        List<String> metrics = result.getMetrics();

        FileInfoDto fileInfo = getFileInfo(file);
        FileUploadEntity fileUploadEntity = FileUploadEntity.builder()
                .id(fileId)
                .fileName(fileInfo.getFileName())
                .fileSize(fileInfo.getFileSize())
                .fileType(fileInfo.getFileType())
                .status(FileUploadStatus.UPLOADED)
                .jsonData(jsonData)
                .metrics(metrics)
                .build();

        FileUploadEntity savedEntity = fileUploadRepository.save(fileUploadEntity);
        return fileUploadMapper.mapToTarget(savedEntity);
    }

    private FileInfoDto getFileInfo(MultipartFile file) {
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("unknown");
        Long fileSize = file.getSize();
        String extension = FilenameUtils.getExtension(fileName);

        FileType fileType;
        if (extension.equals("csv")) {
            fileType = FileType.CSV;
        } else if (extension.equals("pdf")) {
            fileType = FileType.PDF;
        } else {
            fileType = FileType.EXCEL;
        }

        return FileInfoDto.builder()
                .fileName(fileName)
                .fileSize(fileSize)
//                .extension(extension)
                .fileType(fileType)
                .build();
    }
}