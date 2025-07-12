package com.viiku.datavisualizer.util.parser;

import com.google.gson.Gson;
import com.viiku.datavisualizer.model.dtos.ParsedFileResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component
public class ExcelFileParser implements FileParserStrategy {

    @Override
    public boolean supports(String extension) {
        return extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx");
    }

    @Override
    public ParsedFileResult parse(MultipartFile file) throws Exception {
        List<Map<String, String>> data = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                throw new IllegalArgumentException("No header row found in Excel file.");
            }

            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue().trim());
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String value = cell != null ? cell.toString().trim() : "";
                    rowData.put(headers.get(j), value);
                }
                data.add(rowData);
            }
        }

        return ParsedFileResult.builder()
                .jsonData(new Gson().toJson(data))
                .metrics(headers)
                .build();
    }

}
