package com.viiku.datavisualizer.model.dtos;

import com.viiku.datavisualizer.model.enums.files.FileType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDto {

    private String fileName;
    private Long fileSize;
    private FileType fileType;
}
