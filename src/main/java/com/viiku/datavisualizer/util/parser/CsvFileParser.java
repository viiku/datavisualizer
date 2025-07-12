package com.viiku.datavisualizer.util.parser;

import com.viiku.datavisualizer.model.dtos.ParsedFileResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Component
public class CsvFileParser implements FileParserStrategy {


    @Override
    public boolean supports(String extension) {
        return "csv".equalsIgnoreCase(extension);
    }

    @Override
    public ParsedFileResult parse(MultipartFile file) throws Exception {
        log.info("Parsing CSV file: {}", file.getOriginalFilename());

        List<Map<String, String>> rows = new ArrayList<>();
        Set<String> headers = new HashSet<>();

        try (CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .parse(new InputStreamReader(file.getInputStream()))) {

            headers.addAll(parser.getHeaderMap().keySet());

            for (CSVRecord record : parser) {
                Map<String, String> row = new HashMap<>();
                for (String header : headers) {
                    row.put(header, record.get(header));
                }
                rows.add(row);
            }
        }

        // Serialize data and return result
        String jsonData = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(rows);

        return ParsedFileResult.builder()
                .jsonData(jsonData)
                .metrics(new ArrayList<>(headers))  // dynamic detection
                .build();
    }
}
