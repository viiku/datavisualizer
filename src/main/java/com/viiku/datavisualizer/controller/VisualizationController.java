//package com.viiku.visualizer.controller;
//
//import com.viiku.visualizer.model.dtos.payload.request.CreateVisualizationRequest;
//import com.viiku.visualizer.service.VisualizationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/visualization")
//public class VisualizationController {
//
//    private final VisualizationService visualizationService;
//
//    @PostMapping
//    public ResponseEntity<?> generateMap(@RequestBody CreateVisualizationRequest jsonData) {
//        try {
//            var result = visualizationService.generateMapFromJson(jsonData);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Visualization error: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/{visualizationId}")
//    public String getVisualization() {
//        return "test";
//    }
//}
