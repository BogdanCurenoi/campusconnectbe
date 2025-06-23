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
            @PathVariable String personGuid,
            @PathVariable String type) {
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
            if (updatedNotification.isPresent()) {
                return new ResponseEntity<>(updatedNotification.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
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
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
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

    // Admin endpoint: cleanup old notifications
    @DeleteMapping("/cleanup/{daysOld}")
    public ResponseEntity<Void> cleanupOldNotifications(@PathVariable int daysOld) {
        try {
            notificationService.cleanupOldNotifications(daysOld);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test-notification")
    public ResponseEntity<String> testNotification() {
        try {
            NotificationDTO testNotification = new NotificationDTO(
                    "test-user-123",
                    "This is a test notification",
                    "test_type"
            );

            NotificationDTO created = notificationService.createNotification(testNotification);
            return ResponseEntity.ok("Notification created successfully with ID: " + created.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}