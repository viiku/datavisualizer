package com.viiku.datavisualizer.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileProcessingDto {

    private String status;
    private String transformedData;
    private Map<String, Object> statistics;
    private List<String> processingSteps;
    private List<String> errors;
    private int processedRecords;

    public void addStep(String step) {
        if (processingSteps == null) {
            processingSteps = new ArrayList<>();
        }
        processingSteps.add(step);
    }

    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}
