package com.viiku.datavisualizer.service.impl;

import com.viiku.datavisualizer.service.DataVisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataVisualizationServiceImpl implements DataVisualizationService {

    @Override
    public List<Map<String, Object>> getPreparedData(String id) {
        return null;
    }
}
