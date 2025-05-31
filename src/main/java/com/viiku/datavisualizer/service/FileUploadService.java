package com.viiku.datavisualizer.service;

import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    FileUploadResponse uploadFile(MultipartFile file) throws IOFileUploadException;

//    FileUploadResponse parseAndStoreFile(MultipartFile file) throws FileParsingException;
    
//    var generateMapFromJson(MultipartFile file);

//    FileStatusResponse getFileStatus(String uploadId);
}
