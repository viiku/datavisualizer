package com.viiku.datavisualizer.service;

import com.viiku.datavisualizer.model.dtos.payload.response.FileStatusResponse;
import com.viiku.datavisualizer.model.dtos.payload.response.FileUploadResponse;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


public interface FileService {

    FileUploadResponse uploadFile(MultipartFile file) throws IOFileUploadException;

    FileStatusResponse getFileStatus(UUID fileId);

//    FileUploadResponse parseAndStoreFile(MultipartFile file) throws FileParsingException;
    
//    var generateMapFromJson(MultipartFile file);

//    FileStatusResponse getFileStatus(String uploadId);
}
