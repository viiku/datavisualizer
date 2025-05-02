package com.viiku.visualizer.service;

import com.viiku.visualizer.common.exception.FileParsingException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String parseFile(MultipartFile file) throws FileParsingException;

}
