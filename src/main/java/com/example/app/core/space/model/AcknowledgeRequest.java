package com.example.app.core.space.model;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class AcknowledgeRequest {
    private Long spaceId;
    private Long userId;
    private ZonedDateTime acknowledgeTimestamp;
    private Boolean accepted;
}