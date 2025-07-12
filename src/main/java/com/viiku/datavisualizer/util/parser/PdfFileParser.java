package com.viiku.datavisualizer.util.parser;

import com.google.gson.Gson;
import com.viiku.datavisualizer.model.dtos.ParsedFileResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Component
public class PdfFileParser implements FileParserStrategy {

    @Override
    public boolean supports(String extension) {
        return extension.equalsIgnoreCase("pdf");
    }

    @Override
    public ParsedFileResult parse(MultipartFile file) throws Exception {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            String text = new PDFTextStripper().getText(document);
            return ParsedFileResult.builder()
                    .jsonData(new Gson().toJson(Map.of("content", text)))
                    .metrics(List.of("content")) // PDF may not have clear column metrics
                    .build();
        }
    }
}
