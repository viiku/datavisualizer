package com.viiku.visualizer.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.viiku.visualizer.common.exception.FileParsingException;
import com.viiku.visualizer.model.entity.DataFile;
import com.viiku.visualizer.repository.DataFileRepository;
import com.viiku.visualizer.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final DataFileRepository dataFileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String parseFile(MultipartFile file) throws FileParsingException {
        try {
            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();
            String jsonData;

            if (fileName.endsWith(".csv")) {
                jsonData = parseCsv(file);
            } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                jsonData = parseExcel(file);
            } else if (fileName.endsWith(".pdf")) {
                jsonData = parsePdf(file);
            } else {
                throw new FileParsingException("Unsupported file type: " + fileName);
            }

            // Save to DB (optional)
            DataFile dataFile = new DataFile();
            dataFile.setFileName(fileName);
            dataFile.setFileType(fileType);
            dataFile.setJsonData(jsonData);
            dataFileRepository.save(dataFile);

            return jsonData;

        } catch (IOException | IllegalArgumentException e) {
            throw new FileParsingException("Failed to parse file: " + e.getMessage(), e);
        }
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
