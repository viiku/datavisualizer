package com.viiku.datavisualizer.service.impl;

import com.viiku.datavisualizer.service.VisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VisualizationServiceImpl implements VisualizationService {

    @Override
    public List<Map<String, Object>> getPreparedData(String id) {
        return null;
    }
}
