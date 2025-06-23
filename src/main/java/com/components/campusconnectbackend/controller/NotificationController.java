package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.NotificationDTO;
import com.components.campusconnectbackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Create a new notification
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        try {
            NotificationDTO createdNotification = notificationService.createNotification(notificationDTO);
            return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all notifications for a person
    @GetMapping("/person/{personGuid}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByPerson(@PathVariable String personGuid) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsByPersonGuid(personGuid);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get unread notifications for a person
    @GetMapping("/person/{personGuid}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotificationsByPerson(@PathVariable String personGuid) {
        try {
            List<NotificationDTO> notifications = notificationService.getUnreadNotificationsByPersonGuid(personGuid);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get unread count for a person
    @GetMapping("/person/{personGuid}/unread/count")
    public ResponseEntity<Long> getUnreadCountByPerson(@PathVariable String personGuid) {
        try {
            long count = notificationService.getUnreadCountForPerson(personGuid);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get notifications by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByType(@PathVariable String type) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsByType(type);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get notifications for a person by type
    @GetMapping("/person/{personGuid}/type/{type}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByPersonAndType(
            @PathVariable String personGuid, @PathVariable String type) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsByPersonGuidAndType(personGuid, type);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mark notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationDTO> markNotificationAsRead(@PathVariable String notificationId) {
        try {
            Optional<NotificationDTO> updatedNotification = notificationService.markNotificationAsRead(notificationId);
            return updatedNotification
                    .map(notification -> new ResponseEntity<>(notification, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mark all notifications as read for a person
    @PutMapping("/person/{personGuid}/read-all")
    public ResponseEntity<Void> markAllNotificationsAsReadForPerson(@PathVariable String personGuid) {
        try {
            notificationService.markAllNotificationsAsReadForPerson(personGuid);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete notification
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String notificationId) {
        try {
            boolean deleted = notificationService.deleteNotification(notificationId);
            return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete all read notifications for a person
    @DeleteMapping("/person/{personGuid}/read")
    public ResponseEntity<Void> deleteReadNotificationsForPerson(@PathVariable String personGuid) {
        try {
            notificationService.deleteReadNotificationsForPerson(personGuid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Admin: cleanup old notifications
    @DeleteMapping("/cleanup/{daysOld}")
    public ResponseEntity<Void> cleanupOldNotifications(@PathVariable int daysOld) {
        try {
            notificationService.cleanupOldNotifications(daysOld);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // NEW ENDPOINTS FOR no_from_guid functionality

    // Get notifications sent by a specific person
    @GetMapping("/from/{fromGuid}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByFromGuid(@PathVariable String fromGuid) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsByFromGuid(fromGuid);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get notifications sent by a specific person to a specific recipient
    @GetMapping("/person/{personGuid}/from/{fromGuid}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByPersonAndFromGuid(
            @PathVariable String personGuid, @PathVariable String fromGuid) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsByPersonGuidAndFromGuid(personGuid, fromGuid);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get notifications sent by a specific person of a specific type
    @GetMapping("/from/{fromGuid}/type/{type}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByFromGuidAndType(
            @PathVariable String fromGuid, @PathVariable String type) {
        try {
            List<NotificationDTO> notifications = notificationService.getNotificationsByFromGuidAndType(fromGuid, type);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get unread notifications sent by a specific person
    @GetMapping("/from/{fromGuid}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotificationsByFromGuid(@PathVariable String fromGuid) {
        try {
            List<NotificationDTO> notifications = notificationService.getUnreadNotificationsByFromGuid(fromGuid);
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get count of notifications sent by a specific person
    @GetMapping("/from/{fromGuid}/count")
    public ResponseEntity<Long> getNotificationCountByFromGuid(@PathVariable String fromGuid) {
        try {
            long count = notificationService.getNotificationCountByFromGuid(fromGuid);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get system notifications (where noFromGuid is null)
    @GetMapping("/system")
    public ResponseEntity<List<NotificationDTO>> getSystemNotifications() {
        try {
            List<NotificationDTO> notifications = notificationService.getSystemNotifications();
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get user-generated notifications (where noFromGuid is not null)
    @GetMapping("/user-generated")
    public ResponseEntity<List<NotificationDTO>> getUserGeneratedNotifications() {
        try {
            List<NotificationDTO> notifications = notificationService.getUserGeneratedNotifications();
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Test endpoint to create sample notifications
    @GetMapping("/test")
    public ResponseEntity<String> testNotification() {
        try {
            // Create a system notification (no sender)
            NotificationDTO systemNotification = new NotificationDTO(
                    "test-user-guid",
                    null, // No sender - system notification
                    "This is a test system notification",
                    "system"
            );
            notificationService.createNotification(systemNotification);

            // Create a user notification (with sender)
            NotificationDTO userNotification = new NotificationDTO(
                    "test-user-guid",
                    "sender-user-guid", // From another user
                    "This is a test notification from another user",
                    "test_type"
            );
            notificationService.createNotification(userNotification);

            // Create an activity application notification
            NotificationDTO activityNotification = new NotificationDTO(
                    "test-user-guid",
                    "organizer-user-guid",
                    "Someone applied for your activity",
                    "activity_application"
            );
            notificationService.createNotification(activityNotification);

            return new ResponseEntity<>("Test notifications created successfully with no_from_guid field!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create test notifications: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}