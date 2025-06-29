package com.viiku.datavisualizer.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSchema {

    private String fileName;
    List<ColumnInfo> columns;
    private int totalRows;

}
