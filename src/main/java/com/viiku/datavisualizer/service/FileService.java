package com.viiku.datavisualizer.service;

import com.viiku.datavisualizer.model.enums.files.FileStatus;
import com.viiku.datavisualizer.model.enums.files.FileType;
import com.viiku.datavisualizer.model.payload.response.FileListResponse;
import com.viiku.datavisualizer.model.payload.response.FileStatusResponse;
import com.viiku.datavisualizer.model.payload.response.FileUploadResponse;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


public interface FileService {

    FileUploadResponse uploadAndProcessFile(MultipartFile file) throws IOFileUploadException;

    FileStatusResponse getFileStatus(UUID fileId);

    void deleteFile(UUID fileId);

    Object getFilePreview(UUID fileId, int rows);

    FileUploadResponse retryProcessing(UUID fileId);

    FileListResponse getFileList(Pageable pageable, FileStatus status, FileType fileType);
}
