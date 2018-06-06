package com.spdb.service;

import com.spdb.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    @Autowired
    private PredictionRepository predictionRepository;

    public String getPrediction() {
        return predictionRepository.getTest().get(0).getStop();
    }
}
