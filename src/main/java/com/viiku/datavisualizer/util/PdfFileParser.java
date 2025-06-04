package com.viiku.datavisualizer.util;

import com.google.gson.Gson;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class PdfFileParser implements FileParserStrategy {

    @Override
    public String parse(MultipartFile file) throws Exception {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            String text = new PDFTextStripper().getText(document);
            return new Gson().toJson(Map.of("content", text));
        }
    }

    @Override
    public boolean supports(String extension) {
        return extension.equalsIgnoreCase("pdf");
    }
}
