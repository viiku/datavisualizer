package com.viiku.visualizer.service;

import com.viiku.visualizer.model.dtos.payload.response.FileStatusResponse;
import com.viiku.visualizer.model.dtos.payload.response.FileUploadResponse;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    FileUploadResponse uploadFile(MultipartFile file) throws IOFileUploadException;

//    FileUploadResponse parseAndStoreFile(MultipartFile file) throws FileParsingException;
    
//    var generateMapFromJson(MultipartFile file);

    FileStatusResponse getFileStatus(String uploadId);
}
