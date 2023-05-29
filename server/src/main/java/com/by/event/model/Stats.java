package com.by.event.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Stats {
    Integer totalEvents;
    Double next30DaysAverageEventNumber;
    Double favouriteRatio;
}
