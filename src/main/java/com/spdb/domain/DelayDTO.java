package com.spdb.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DelayDTO {
    String nearestScheduledBus;
    String scheduledEndStopArrival;
    String averageRealEndStopArrival;
    String averageDelaysEndStopArrival;
}
