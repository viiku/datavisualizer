package com.viiku.datavisualizer.model.dtos;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ParsedFileResult {
    private String jsonData;
    private List<String> metrics;
}
