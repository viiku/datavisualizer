package com.viiku.datavisualizer.util;

import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Component
public class CsvFileParser implements FileParserStrategy {

    @Override
    public String parse(MultipartFile file) throws Exception {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                    .withIgnoreHeaderCase().withTrim().parse(reader);

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

    @Override
    public boolean supports(String extension) {
        return extension.equalsIgnoreCase("csv");
    }
}
