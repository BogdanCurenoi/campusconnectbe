package com.components.campusconnectbackend.repository.mongo;

import com.components.campusconnectbackend.domain.mongo.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    // Find all notifications for a specific person
    List<Notification> findByNoGuid(String noGuid);

    // Find unread notifications for a specific person
    List<Notification> findByNoGuidAndNoReadFalse(String noGuid);

    // Find notifications by type
    List<Notification> findByNoType(String noType);

    // Find notifications for a person by type
    List<Notification> findByNoGuidAndNoType(String noGuid, String noType);

    // Find unread notifications for a person by type
    List<Notification> findByNoGuidAndNoTypeAndNoReadFalse(String noGuid, String noType);

    // Count unread notifications for a person
    long countByNoGuidAndNoReadFalse(String noGuid);

    // Find notifications created after a specific date
    List<Notification> findByNoCreatedAtAfter(LocalDateTime dateTime);

    // Find notifications for a person created after a specific date
    List<Notification> findByNoGuidAndNoCreatedAtAfter(String noGuid, LocalDateTime dateTime);

    // Custom query to find notifications ordered by creation date (newest first)
    @Query(value = "{ 'no_guid': ?0 }", sort = "{ 'no_created_at': -1 }")
    List<Notification> findByNoGuidOrderByNoCreatedAtDesc(String noGuid);

    // Delete all read notifications for a person
    void deleteByNoGuidAndNoReadTrue(String noGuid);

    // Delete notifications older than a specific date
    void deleteByNoCreatedAtBefore(LocalDateTime dateTime);
}