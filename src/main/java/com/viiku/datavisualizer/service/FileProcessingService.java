package com.viiku.datavisualizer.service;

import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileProcessingService {

    FileUploadResponse processFileAsync(MultipartFile file) throws IOFileUploadException;

    Map<String, Object> getProcessingStatus(String processingId);

//    FileUploadResponse parseAndStoreFile(MultipartFile file) throws FileParsingException;
    
//    var generateMapFromJson(MultipartFile file);

//    FileStatusResponse getFileStatus(String uploadId);
}
