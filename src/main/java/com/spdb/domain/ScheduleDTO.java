package com.spdb.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScheduleDTO {
    Long way;
    Long order;
    Double avgDelay;
    String scheduleDeparture;
    String avgRealDeparture;
    String lineName;
    Long courseId;
    Long dayType;
    Long stopId;
}
