package com.viiku.datavisualizer.util.builder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viiku.datavisualizer.model.dtos.ParsedFileResult;
import com.viiku.datavisualizer.model.entities.FileContentEntity;
import com.viiku.datavisualizer.model.entities.FileUploadEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileContentBuilder {


    private final ObjectMapper objectMapper;

    public List<FileContentEntity> buildFileContentEntities(ParsedFileResult result, FileUploadEntity fileUpload) {
        List<FileContentEntity> entities = new ArrayList<>();

        try {
            List<Map<String, String>> rows = objectMapper.readValue(
                    result.getJsonData(),
                    new TypeReference<>() {
                    }
            );

            int rowIndex = 0;
            for (Map<String, String> row : rows) {
                String rowJson = objectMapper.writeValueAsString(row);
                String searchable = String.join(" ", row.values());
                String rowHash = generateHash(rowJson);

                FileContentEntity entity = FileContentEntity.builder()
                        .fileUpload(fileUpload)
                        .rowIndex(rowIndex++)
                        .rowDataJson(rowJson)
                        .searchableContent(searchable)
                        .rowHash(rowHash)
                        .build();

                entities.add(entity);
            }

        } catch (Exception e) {
            log.error("Failed to parse rows from ParsedFileResult", e);
            throw new RuntimeException("Failed to build file content entities", e);
        }

        return entities;

    }

    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            log.warn("Could not compute hash for row: {}", e.getMessage());
            return null;
        }
    }
}
