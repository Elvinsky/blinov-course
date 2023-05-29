package com.by.event.request;

import java.util.Date;

public record CreateCommentRequest(String date,
                                   String commentText,
                                   Integer eventId) {
}
