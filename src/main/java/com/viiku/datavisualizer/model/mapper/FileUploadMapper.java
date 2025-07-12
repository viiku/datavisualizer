package com.viiku.datavisualizer.model.mapper;

import com.viiku.datavisualizer.common.model.mapper.BaseMapper;
import com.viiku.datavisualizer.model.payload.response.FileUploadResponse;
import com.viiku.datavisualizer.model.entities.FileUploadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface FileUploadMapper extends BaseMapper<FileUploadEntity, FileUploadResponse> {

    @Mapping(source = "id", target = "fileId")
    @Mapping(source = "id", target = "datasetId")
    @Mapping(source = "createdAt", target = "uploadTimestamp")
    @Mapping(source = "status", target = "uploadStatus")
    @Mapping(source = "columnHeaders", target = "detectedMetrics")
    FileUploadResponse mapToTarget(FileUploadEntity uploadEntity);

    // Optional if needed
    FileUploadEntity mapToEntity(FileUploadResponse response);
}
