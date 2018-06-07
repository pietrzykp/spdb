package com.spdb.controller;

import com.spdb.service.LineInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lineinfo")
public class LineInfoController {

    @Autowired
    private LineInfoService lineInfoService;


    @GetMapping("/linestring={line}")
    public ResponseEntity<String> getLineString(@PathVariable("line") String line) {
        return ResponseEntity.ok(lineInfoService.getStopsListString(line));
    }

    @GetMapping("/linejson={line}")
    public ResponseEntity<String> getLineJson(@PathVariable("line") String line) {
        return ResponseEntity.ok(lineInfoService.getStopsListJson(line));
    }
}
