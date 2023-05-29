package com.by.event.request;

public record UpdateEventRequest(String title,
                                 String description,
                                 Integer participants,
                                 String startDate,
                                 String endDate,
                                 boolean isFavourite) {
}
