package com.viiku.datavisualizer.util;

import org.springframework.web.multipart.MultipartFile;

public interface FileParserStrategy {
    String parse(MultipartFile file) throws Exception;
    boolean supports(String extension); // for matching file types
}
