package com.spdb.service;

import com.google.gson.Gson;
import com.spdb.domain.DelayDTO;
import com.spdb.domain.ScheduleDTO;
import com.spdb.repository.PredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PredictionService {

    @Autowired
    private PredictionRepository predictionRepository;

    public String getPrediction(String lineName, Long way, Long beginStop, Long endStop, String startTime) {

        String s = "2019-02-02 " + startTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        LocalDateTime date = LocalDateTime.parse(s, formatter);

        LocalDateTime start = date.minusMinutes(30);
        LocalDateTime end = date.plusMinutes(120);

        String startString = start.format(formatter);
        String endString = end.format(formatter);

        if(start.getDayOfYear() != date.getDayOfYear()) {
            startString = "2019-02-02 00:00:00";
        }

        if(end.getDayOfYear() != date.getDayOfYear()) {
            endString = "2019-02-02 23:59:59";
        }

        List<ScheduleDTO> scheduleDTOList = predictionRepository.getScheduleInfo(startString, endString, 103L, lineName, way);

        ScheduleDTO startDTO = findNearestCourseId(date, beginStop, scheduleDTOList);

        ScheduleDTO scheduledEndDTO = countScheduledTime(scheduleDTOList, startDTO, endStop);
        if(scheduledEndDTO == null) {
            return "Something fishy happened";
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);
        LocalDateTime realScheduleEnd = countRealTime(scheduleDTOList, scheduledEndDTO);

        LocalDateTime delaysEnd = countDelays(scheduleDTOList, startDTO, beginStop, endStop);

        DelayDTO delayDTO = DelayDTO.builder()
                .nearestScheduledBus(startDTO.getScheduleDeparture())
                .scheduledEndStopArrival(scheduledEndDTO.getScheduleDeparture())
                .averageRealEndStopArrival(realScheduleEnd.format(timeFormatter))
                .averageDelaysEndStopArrival(delaysEnd.format(timeFormatter))
                .build();

        Gson gson = new Gson();
        String json = gson.toJson(delayDTO);
        return json;
    }

    private LocalDateTime countDelays(List<ScheduleDTO> scheduleDTOList, ScheduleDTO startDTO, Long beginStop, Long endStop) {
        Map<String, ScheduleDTO> map = new HashMap<>();
        Long scheduleCourse = startDTO.getCourseId();
        for(ScheduleDTO scheduleDTO: scheduleDTOList) {
            if(scheduleDTO.getCourseId().equals(scheduleCourse) && scheduleDTO.getOrder() >= beginStop && scheduleDTO.getOrder() <= endStop) {
                map.put(scheduleDTO.getOrder().toString() + " " + scheduleDTO.getScheduleDeparture(), scheduleDTO);
            }
        }

        Map<Long, Double> resultMap = new HashMap<>();
        Map<Long, ScheduleDTO> courseMap = new HashMap<>();
        for(ScheduleDTO scheduleDTO: scheduleDTOList) {
            if(map.containsKey(scheduleDTO.getOrder().toString() + " " + scheduleDTO.getScheduleDeparture())) {
                Long order = scheduleDTO.getOrder();
                Double result = resultMap.getOrDefault(order, 0.0);
                resultMap.put(order, result + scheduleDTO.getAvgDelay());
                if(scheduleDTO.getCourseId().equals(scheduleCourse)) {
                    courseMap.put(order, scheduleDTO);
                }
            }
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        double lastDelay = resultMap.get(beginStop);
        LocalDateTime lastScheduleStop = LocalDateTime.parse("2019-02-02 " + courseMap.get(beginStop).getScheduleDeparture(), formatter);
        LocalDateTime lastRealScheduleStop = lastScheduleStop.plusSeconds((long)lastDelay);

        for(long i = beginStop + 1; i <= endStop; ++i) {
            ScheduleDTO scheduleDTO = courseMap.get(new Long(i));
            LocalDateTime scheduledStop = LocalDateTime.parse("2019-02-02 " + scheduleDTO.getScheduleDeparture(), formatter);
            double l1 = Duration.between(lastScheduleStop, scheduledStop).toMillis() / 1000;
            Double delay = resultMap.get(new Long(i));
            double delaydiff = delay - lastDelay;
            double wholedelay = l1 + delaydiff;
            lastRealScheduleStop = lastRealScheduleStop.plusSeconds((long)wholedelay);
            lastScheduleStop = scheduledStop;
            lastDelay = delay;
        }
        
        return lastRealScheduleStop;
    }

    private LocalDateTime countRealTime(List<ScheduleDTO> scheduleDTOList, ScheduleDTO scheduledEndDTO) {
        Long orderToFind = scheduledEndDTO.getOrder();
        String scheduledTimeToFind = scheduledEndDTO.getScheduleDeparture();
        List<ScheduleDTO> realArrivals = new LinkedList<>();
        for(ScheduleDTO scheduleDTO: scheduleDTOList) {
            if(scheduleDTO.getOrder().equals(orderToFind) && scheduleDTO.getScheduleDeparture().equals(scheduledTimeToFind)) {
                realArrivals.add(scheduleDTO);
            }
        }

        long seconds = 0L;
        for(ScheduleDTO scheduleDTO: realArrivals) {
            String[] split = scheduleDTO.getAvgRealDeparture().substring(0, 8).split(":");
            seconds += Integer.valueOf(split[0]) * 60 * 60;
            seconds += Integer.valueOf(split[1]) * 60;
            seconds += Integer.valueOf(split[2]);
        }
        seconds /= realArrivals.size();
        long hh = seconds / 60 / 60;
        long mm = (seconds / 60) % 60;
        long ss = seconds % 60;

        LocalDateTime localDateTime = LocalDateTime.of(2019, 2, 2, (int)hh, (int)mm, (int)ss);
        return localDateTime;
    }

    private ScheduleDTO countScheduledTime(List<ScheduleDTO> scheduleDTOList, ScheduleDTO startDTO, Long endStop) {
        Long courseId = startDTO.getCourseId();
        for(ScheduleDTO scheduleDTO: scheduleDTOList) {
            if(!scheduleDTO.getCourseId().equals(courseId))
                continue;
            if(!scheduleDTO.getOrder().equals(endStop))
                continue;
            return scheduleDTO;
        }
        return null;
    }

    private ScheduleDTO findNearestCourseId(LocalDateTime start, Long beginStop, List<ScheduleDTO> scheduleDTOList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        LocalDateTime foundSchedule = null;
        ScheduleDTO nearestScheduleDTO = null;
        for(ScheduleDTO scheduleDTO: scheduleDTOList) {
            if(!scheduleDTO.getOrder().equals(beginStop))
                continue;
            String fulldate = "2019-02-02 " + scheduleDTO.getScheduleDeparture();
            LocalDateTime scheduleTime = LocalDateTime.parse(fulldate, formatter);
            if (scheduleTime.isAfter(start)) {
                if(foundSchedule == null || scheduleTime.isBefore(foundSchedule)) {
                    foundSchedule = scheduleTime;
                    nearestScheduleDTO = scheduleDTO;
                }
            }

        }
        return nearestScheduleDTO;
    }
}
