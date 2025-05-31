package com.viiku.datavisualizer.service.impl;

import com.viiku.datavisualizer.common.exception.FileUploadException;
import com.viiku.datavisualizer.model.dtos.payload.response.FileStatusResponse;
import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
import com.viiku.datavisualizer.model.enums.FileStatus;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.viiku.datavisualizer.common.exception.FileParsingException;
import com.viiku.datavisualizer.model.entities.FileDataEntity;
import com.viiku.datavisualizer.repository.FileDataRepository;
import com.viiku.datavisualizer.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final FileDataRepository fileDataRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file) throws IOFileUploadException {

        if (file.isEmpty()) {
            throw new FileUploadException("File is empty: " + file.getOriginalFilename());
        }

        try {
            String fileId = parseAndStoreFile(file);
            return new FileUploadResponse(fileId, "File uploaded successfully");
        } catch (FileParsingException e) {
            throw new FileUploadException("File parsing failed: " + e.getMessage());
        } catch (Exception e) {
            throw new FileUploadException("Unexpected error during file upload: " + e.getMessage());
        }
    }

//    @Override
//    public var generateMapFromJson(MultipartFile file) {
//        return null;
//    }

    @Override
    public FileStatusResponse getFileStatus(String uploadId) {
        return null;
    }

    private String parseAndStoreFile(MultipartFile file) throws FileParsingException {
        try {
            String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("unknown");
            String fileType = Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");

            String jsonData = switch (getExtension(fileName)) {
                case "csv" -> parseCsv(file);
                case "xls", "xlsx" -> parseExcel(file);
                case "pdf" -> parsePdf(file);
                default -> throw new FileParsingException("Unsupported file type: " + fileName);
            };

            String fileId = UUID.randomUUID().toString();

            FileDataEntity fileData = FileDataEntity.builder()
                    .id(fileId)
                    .fileName(fileName)
                    .fileType(fileType)
                    .jsonData(jsonData)
                    .status(FileStatus.PENDING)
                    .build();

            fileDataRepository.save(fileData);
            return fileId;

        } catch (IOException | IllegalArgumentException e) {
            throw new FileParsingException("Failed to parse file: " + e.getMessage(), e);
        }
    }

    private String getExtension(String fileName) {
        return fileName.contains(".") ?
                fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase() : "";
    }


    private String parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();
            Iterable<CSVRecord> records = format.parse(reader);
            List<Map<String, String>> data = new ArrayList<>();

            for (CSVRecord record : records) {
                Map<String, String> row = new HashMap<>();
                for (String header : record.toMap().keySet()) {
                    row.put(header, record.get(header));
                }
                data.add(row);
            }

            return new Gson().toJson(data);
        }
    }

    private String parseExcel(MultipartFile file) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
                    rowData.put(headerRow.getCell(j).getStringCellValue(), row.getCell(j).toString());
                }
                data.add(rowData);
            }
        }
        return new Gson().toJson(data);
    }

    private String parsePdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            return new Gson().toJson(Map.of("content", text));
        }
    }

    // Similar methods for Excel (Apache POI) & PDF (PDFBox)
}
