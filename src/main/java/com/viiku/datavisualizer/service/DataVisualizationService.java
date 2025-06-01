package com.viiku.datavisualizer.service;

import java.util.List;
import java.util.Map;

public interface DataVisualizationService {

    List<Map<String, Object>> getPreparedData(String id);
}
