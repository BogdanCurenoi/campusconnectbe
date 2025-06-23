package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.mongo.Notification;
import com.components.campusconnectbackend.dto.NotificationDTO;
import com.components.campusconnectbackend.repository.mongo.NotificationRepository;
import com.components.campusconnectbackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification;

        // Create notification with or without noFromGuid
        if (notificationDTO.getNoFromGuid() != null) {
            notification = new Notification(
                    notificationDTO.getNoGuid(),
                    notificationDTO.getNoFromGuid(),
                    notificationDTO.getNoMessage(),
                    notificationDTO.getNoType()
            );
        } else {
            notification = new Notification(
                    notificationDTO.getNoGuid(),
                    notificationDTO.getNoMessage(),
                    notificationDTO.getNoType()
            );
        }

        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    @Override
    public List<NotificationDTO> getNotificationsByPersonGuid(String personGuid) {
        List<Notification> notifications = notificationRepository.findByNoGuidOrderByNoCreatedAtDesc(personGuid);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getUnreadNotificationsByPersonGuid(String personGuid) {
        List<Notification> notifications = notificationRepository.findByNoGuidAndNoReadFalse(personGuid);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByType(String type) {
        List<Notification> notifications = notificationRepository.findByNoType(type);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByPersonGuidAndType(String personGuid, String type) {
        List<Notification> notifications = notificationRepository.findByNoGuidAndNoType(personGuid, type);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NotificationDTO> markNotificationAsRead(String notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);

        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setNoRead(true);
            Notification updatedNotification = notificationRepository.save(notification);
            return Optional.of(convertToDTO(updatedNotification));
        }

        return Optional.empty();
    }

    @Override
    public void markAllNotificationsAsReadForPerson(String personGuid) {
        List<Notification> unreadNotifications = notificationRepository.findByNoGuidAndNoReadFalse(personGuid);

        for (Notification notification : unreadNotifications) {
            notification.setNoRead(true);
        }

        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public long getUnreadCountForPerson(String personGuid) {
        return notificationRepository.countByNoGuidAndNoReadFalse(personGuid);
    }

    @Override
    public boolean deleteNotification(String notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            notificationRepository.deleteById(notificationId);
            return true;
        }
        return false;
    }

    @Override
    public void deleteReadNotificationsForPerson(String personGuid) {
        notificationRepository.deleteByNoGuidAndNoReadTrue(personGuid);
    }

    @Override
    public void cleanupOldNotifications(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        notificationRepository.deleteByNoCreatedAtBefore(cutoffDate);
    }

    // NEW METHODS FOR no_from_guid functionality

    @Override
    public List<NotificationDTO> getNotificationsByFromGuid(String fromGuid) {
        List<Notification> notifications = notificationRepository.findByNoFromGuidOrderByNoCreatedAtDesc(fromGuid);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByPersonGuidAndFromGuid(String personGuid, String fromGuid) {
        List<Notification> notifications = notificationRepository.findByNoGuidAndNoFromGuid(personGuid, fromGuid);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotificationsByFromGuidAndType(String fromGuid, String type) {
        List<Notification> notifications = notificationRepository.findByNoFromGuidAndNoType(fromGuid, type);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getUnreadNotificationsByFromGuid(String fromGuid) {
        List<Notification> notifications = notificationRepository.findByNoFromGuidAndNoReadFalse(fromGuid);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getNotificationCountByFromGuid(String fromGuid) {
        return notificationRepository.countByNoFromGuid(fromGuid);
    }

    @Override
    public List<NotificationDTO> getSystemNotifications() {
        List<Notification> notifications = notificationRepository.findByNoFromGuidIsNull();
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getUserGeneratedNotifications() {
        List<Notification> notifications = notificationRepository.findByNoFromGuidIsNotNull();
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Notification entity to DTO
    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getNoGuid(),
                notification.getNoFromGuid(), // Include the new field
                notification.getNoMessage(),
                notification.getNoType(),
                notification.getNoRead(),
                notification.getNoCreatedAt()
        );
    }
}