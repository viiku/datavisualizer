package com.viiku.datavisualizer.model.mapper;

import com.viiku.datavisualizer.common.model.mapper.BaseMapper;
import com.viiku.datavisualizer.model.dtos.CsvDataDto;
import com.viiku.datavisualizer.model.entities.CsvDataEntity;

public class CsvDataMapper implements BaseMapper<CsvDataEntity, CsvDataDto> {

    @Override
    public CsvDataDto mapToTarget(CsvDataEntity entity) {
        return null;
    }

    @Override
    public CsvDataEntity mapToEntity(CsvDataDto target) {
        return null;
    }
}
