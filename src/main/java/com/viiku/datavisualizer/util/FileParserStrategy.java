package com.viiku.datavisualizer.util;

import com.viiku.datavisualizer.model.dtos.ParsedFileResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileParserStrategy {
    ParsedFileResult parse(MultipartFile file) throws Exception;
    boolean supports(String extension); // for matching file types
}
