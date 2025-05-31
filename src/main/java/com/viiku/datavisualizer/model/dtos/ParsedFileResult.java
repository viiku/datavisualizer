package com.viiku.datavisualizer.model.dtos;

import lombok.*;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParsedFileResult {
    private String fileName;
    private String fileType;
    private List<Map<String, String>> records;
}
