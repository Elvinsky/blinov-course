package com.by.event.request;

import lombok.Builder;

@Builder
public record CreateEventRequest(String title,
                                 String description,
                                 Integer participants,
                                 String startDate,
                                 String endDate) {
}
