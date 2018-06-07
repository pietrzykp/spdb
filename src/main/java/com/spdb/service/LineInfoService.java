package com.spdb.service;

import com.google.gson.Gson;
import com.spdb.domain.LineInfoDTO;
import com.spdb.repository.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineInfoService {

    @Autowired
    private LineRepository lineRepository;

    public String getStopsListString(String line) {
        List<LineInfoDTO> lineInfoDTOS = lineRepository.getScheduleInfo(line);
        StringBuilder builder = new StringBuilder();
        long last = -1;
        for(LineInfoDTO lineInfo :lineInfoDTOS) {
            if (lineInfo.getWay().longValue() != last) {
                last = lineInfo.getWay().longValue();
                builder.append("_____Kierunek " + last + "_____:");
            }
            builder.append("(" + lineInfo.getOrder() + ", " + lineInfo.getStop() + ") ");
        }
        return builder.toString();
    }

    public String getStopsListJson(String line) {
        List<LineInfoDTO> lineInfoDTOS = lineRepository.getScheduleInfo(line);
        Gson gson = new Gson();
        String json = gson.toJson(lineInfoDTOS);
        return json;
    }


}
