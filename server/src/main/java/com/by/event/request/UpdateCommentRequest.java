package com.by.event.request;

import java.util.Date;

public record UpdateCommentRequest(String date,
                                   Integer eventId,
                                   String commentText) {
}
