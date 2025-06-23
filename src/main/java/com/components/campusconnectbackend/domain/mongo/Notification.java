package com.components.campusconnectbackend.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "cc_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id; // MongoDB auto-generated ID

    @Field("no_guid")
    private String noGuid; // GUID of the person

    @Field("no_message")
    private String noMessage; // Message of the notification

    @Field("no_type")
    private String noType; // Type of notification (e.g., "activity_application")

    @Field("no_read")
    private Boolean noRead = false; // Whether the notification has been read (default: false)

    @Field("no_created_at")
    private LocalDateTime noCreatedAt = LocalDateTime.now(); // Creation timestamp

    // Constructor without read status and timestamp (they'll use defaults)
    public Notification(String noGuid, String noMessage, String noType) {
        this.noGuid = noGuid;
        this.noMessage = noMessage;
        this.noType = noType;
        this.noRead = false;
        this.noCreatedAt = LocalDateTime.now();
    }
}