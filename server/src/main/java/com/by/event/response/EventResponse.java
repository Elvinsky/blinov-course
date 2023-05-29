package com.by.event.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private Integer id;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String ownerId;
    private boolean isFavourite;
}
