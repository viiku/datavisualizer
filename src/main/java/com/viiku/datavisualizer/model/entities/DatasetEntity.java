package com.viiku.datavisualizer.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_data_uploads")
public class DatasetEntity {

    @Id
    @GeneratedValue
    private UUID datasetId;

    private UUID fileId;

}
