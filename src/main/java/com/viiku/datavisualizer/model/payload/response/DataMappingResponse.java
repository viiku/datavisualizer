package com.viiku.datavisualizer.model.payload.response;

import com.viiku.datavisualizer.model.enums.ColumnDataType;
import com.viiku.datavisualizer.model.enums.ColumnRole;
import com.viiku.datavisualizer.model.enums.FileStatus;
import com.viiku.datavisualizer.model.enums.FileType;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataMappingResponse {

    private UUID datasetId;
    private String fileName;
    private FileType fileType;
    private int totalRows;
    private FileStatus status;
    private List<Columns> columnsList;
    private GeographicalContext context;
    private DataQuality dataQuality;

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Columns {
        private String name;
        private String originalName;
        private ColumnDataType dataType;
        private ColumnRole role;
        private String geographicalLevel;
        private List<String> sampleValues;
        private int uniqueValues;
        private int nullCount;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class GeographicalContext {
        private String detectedCountry;
        private String detectedLevel;
        private int matchedLocations;
        private int unmatchedLocations;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DataQuality {
        private float overallScore;
        private List<String> issues;
        private List<String> recommendations;
    }
}
