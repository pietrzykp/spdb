package com.spdb.controller;

import com.spdb.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/predict")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Dupa");
    }

    @GetMapping("/line/{line}/way/{way}/begin/{begin}/end/{end}/starttime/{starttime}")
    public ResponseEntity<String> getPredictions(@PathVariable("line") String lineName,
                                                 @PathVariable("way") Long way,
                                                 @PathVariable("begin") Long beginStop,
                                                 @PathVariable("end") Long endStop,
                                                 @PathVariable("starttime") String startTime) {
        return ResponseEntity.ok(predictionService.getPrediction(lineName, way, beginStop, endStop, startTime));
    }
}
