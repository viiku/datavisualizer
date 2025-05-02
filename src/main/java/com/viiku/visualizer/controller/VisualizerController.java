package com.viiku.visualizer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class named {@link VisualizerController},
 * expose endpoints for uploading csv files
 */

@RestController
@RequestMapping("/api/v1/csv")
public class VisualizerController {

    @PostMapping("/upload")
    public String upload() {
        return "test";
    }

    @PostMapping("/generateMap")
    public String generate() {
        return "test1";
    }
}
