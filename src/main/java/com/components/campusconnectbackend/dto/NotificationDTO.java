package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String id;
    private String noGuid;
    private String noFromGuid; // New optional field
    private String noMessage;
    private String noType;
    private Boolean noRead;
    private LocalDateTime noCreatedAt;

    // Constructor for creating new notifications (without id, read status, and timestamp)
    public NotificationDTO(String noGuid, String noMessage, String noType) {
        this.noGuid = noGuid;
        this.noMessage = noMessage;
        this.noType = noType;
    }

    // Constructor for creating new notifications with fromGuid
    public NotificationDTO(String noGuid, String noFromGuid, String noMessage, String noType) {
        this.noGuid = noGuid;
        this.noFromGuid = noFromGuid;
        this.noMessage = noMessage;
        this.noType = noType;
    }
}