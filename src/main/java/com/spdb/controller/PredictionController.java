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

    @GetMapping("/lineName/{lineName}/begin/{beginstop}/end/{endstop}")
    public ResponseEntity<String> getPredictions(@PathVariable("lineName") String lineName,
                                                 @PathVariable("beginstop") String beginStop,
                                                 @PathVariable("endstop") String endStop) {
        return ResponseEntity.ok(predictionService.getPrediction());
    }
}
