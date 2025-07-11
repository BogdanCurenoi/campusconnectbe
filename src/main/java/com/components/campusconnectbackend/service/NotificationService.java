package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.NotificationDTO;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    // Create a new notification
    NotificationDTO createNotification(NotificationDTO notificationDTO);

    // Get all notifications for a person
    List<NotificationDTO> getNotificationsByPersonGuid(String personGuid);

    // Get unread notifications for a person
    List<NotificationDTO> getUnreadNotificationsByPersonGuid(String personGuid);

    // Get notifications by type
    List<NotificationDTO> getNotificationsByType(String type);

    // Get notifications for a person by type
    List<NotificationDTO> getNotificationsByPersonGuidAndType(String personGuid, String type);

    // Mark notification as read
    Optional<NotificationDTO> markNotificationAsRead(String notificationId);

    // Mark all notifications as read for a person
    void markAllNotificationsAsReadForPerson(String personGuid);

    // Get unread count for a person
    long getUnreadCountForPerson(String personGuid);

    // Delete notification
    boolean deleteNotification(String notificationId);

    // Delete all read notifications for a person
    void deleteReadNotificationsForPerson(String personGuid);

    // Clean up old notifications (older than specified days)
    void cleanupOldNotifications(int daysOld);

    // NEW METHODS FOR no_from_guid functionality

    // Get notifications sent by a specific person
    List<NotificationDTO> getNotificationsByFromGuid(String fromGuid);

    // Get notifications sent by a specific person to a specific recipient
    List<NotificationDTO> getNotificationsByPersonGuidAndFromGuid(String personGuid, String fromGuid);

    // Get notifications sent by a specific person of a specific type
    List<NotificationDTO> getNotificationsByFromGuidAndType(String fromGuid, String type);

    // Get unread notifications sent by a specific person
    List<NotificationDTO> getUnreadNotificationsByFromGuid(String fromGuid);

    // Get count of notifications sent by a specific person
    long getNotificationCountByFromGuid(String fromGuid);

    // Get system notifications (where noFromGuid is null)
    List<NotificationDTO> getSystemNotifications();

    // Get user-generated notifications (where noFromGuid is not null)
    List<NotificationDTO> getUserGeneratedNotifications();
}