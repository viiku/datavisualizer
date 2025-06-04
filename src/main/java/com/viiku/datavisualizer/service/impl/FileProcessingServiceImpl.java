package com.viiku.datavisualizer.service.impl;

import com.viiku.datavisualizer.common.exception.FileParsingException;
import com.viiku.datavisualizer.common.exception.FileUploadException;
import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
import com.viiku.datavisualizer.model.entities.CsvDataEntity;
import com.viiku.datavisualizer.model.enums.FileUploadStatus;
import com.viiku.datavisualizer.repository.CsvDataRepository;
import com.viiku.datavisualizer.service.FileProcessingService;
import com.viiku.datavisualizer.util.CsvFileParser;
import com.viiku.datavisualizer.util.ExcelFileParser;
import com.viiku.datavisualizer.util.FileParserStrategy;
import com.viiku.datavisualizer.util.PdfFileParser;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileProcessingServiceImpl implements FileProcessingService {

    private final List<FileParserStrategy> parserStrategies;
    private final CsvDataRepository csvDataRepository;

    @Override
    public FileUploadResponse processFileAsync(MultipartFile file) {
        if (file.isEmpty()) {
            return FileUploadResponse.builder()
                    .status(FileUploadStatus.FAILED)
                    .message("File is empty.")
                    .build();
        }

        try {
            UUID fileId = parseAndStore(file);
            return FileUploadResponse.builder()
                    .fileId(fileId)
                    .status(FileUploadStatus.PROCESSING)
                    .message("File uploaded successfully")
                    .build();
        } catch (FileParsingException e) {
            throw new FileUploadException("Parsing failed: " + e.getMessage());
        } catch (Exception e) {
            throw new FileUploadException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProcessingStatus(String processingId) {
        return Map.of();
    }

    private UUID parseAndStore(MultipartFile file) throws Exception {
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("unknown");
        String extension = getExtension(fileName);

        FileParserStrategy parser = parserStrategies.stream()
                .filter(p -> p.supports(extension))
                .findFirst()
                .orElseThrow(() -> new FileParsingException("Unsupported file type: " + extension));

        String jsonData = parser.parse(file);
        UUID fileId = UUID.randomUUID();

        CsvDataEntity entity = CsvDataEntity.builder()
                .id(fileId)
                .fileName(fileName)
                .fileType(extension)
                .jsonData(jsonData)
                .status(FileUploadStatus.PENDING)
                .build();

        csvDataRepository.save(entity);
        return fileId;
    }

    private String getExtension(String fileName) {
        return fileName.contains(".") ?
                fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase() : "";
    }
}
