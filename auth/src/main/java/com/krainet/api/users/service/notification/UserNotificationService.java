package com.krainet.api.users.service.notification;

import com.krainet.common.notification.api.NotificationServiceClient;
import com.krainet.common.notification.dto.NotificationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private final NotificationServiceClient notificationServiceClient;

    public void sendNotificationAfterCommit(String eventType, String username, String password,
                                            String email, String performedBy, List<String> adminEmails) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    sendUserEvent(eventType, username, password, email, performedBy, adminEmails);
                }
            });
        } else {
            sendUserEvent(eventType, username, password, email, performedBy, adminEmails);
        }
    }

    @Async
    public CompletableFuture<Void> sendUserEvent(String eventType, String username, String password,
                                                 String email, String performedBy, List<String> adminEmails) {
        try {
            String message = buildNotificationMessage(eventType, username, password, email, performedBy);
            NotificationRequestDto request = new NotificationRequestDto(eventType, username, password, email, performedBy, message);

            ResponseEntity<String> response = notificationServiceClient.sendNotification(request, adminEmails);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Notification service returned error: " + response.getStatusCode());
            }

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            log.error("Failed to send notification for user: {}, error: {}", username, e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    private String buildNotificationMessage(String eventType, String username, String password,
                                            String email, String performedBy) {
        String action = getEventTypeRussian(eventType);
        return String.format("%s пользователь с именем - %s, паролем - %s и почтой - %s. Действие выполнено: %s",
                action, username, password, email, performedBy);
    }

    private String getEventTypeRussian(String eventType) {
        return switch (eventType) {
            case "CREATED" -> "Создан";
            case "UPDATED" -> "Изменен";
            case "DELETED" -> "Удален";
            default -> eventType;
        };
    }
}