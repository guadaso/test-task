package com.krainet.api.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.krainet.api.notification.dto.request.NotificationRequestDto;
import com.krainet.api.notification.dto.response.NotificationResponseDto;
import com.krainet.api.notification.entity.Notification;
import com.krainet.api.notification.repository.NotificationRepository;
import com.krainet.common.messages.LogMessage;
import com.krainet.common.logs.LogService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private final LogService logService;

    @Transactional
    public void sendNotification(NotificationRequestDto request, List<String> emails) {
        logService.logAction(LogMessage.NOTIFICATION_PROCESSING_START, request.getUsername(), true);
        log.info("Received notification request: {} for emails: {}", request, emails);

        Notification notification = createNotificationEntity(request, "PROCESSING");
        try {
            if (emails == null || emails.isEmpty()) {
                logService.logAction(LogMessage.NOTIFICATION_EMPTY_EMAILS, request.getUsername(), false);
                handleEmptyEmails(notification);
                return;
            }
            sendNotificationToRecipients(request, emails);
            handleSuccess(notification, emails.size());
            logService.logAction(LogMessage.NOTIFICATION_PROCESSING_END, request.getUsername(), true);
            log.info("Notification emails sent to {} recipients for user: {}", emails.size(), request.getUsername());

        } catch (Exception e) {
            handleFailure(notification, e, request.getUsername());
            throw new RuntimeException("Failed to process notification request", e);
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getAllNotifications() {
        log.info("Fetching all notifications from database");
        List<Notification> notifications = notificationRepository.findAllByOrderByCreatedAtDesc();
        return notifications.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private NotificationResponseDto convertToDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setEventType(notification.getEventType());
        dto.setUsername(notification.getUsername());
        dto.setEmail(notification.getEmail());
        dto.setPerformedBy(notification.getPerformedBy());
        dto.setMessage(notification.getMessage());
        dto.setStatus(notification.getStatus());
        dto.setRecipientCount(notification.getRecipientCount());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setSentAt(notification.getSentAt());
        return dto;
    }

    private Notification createNotificationEntity(NotificationRequestDto request, String status) {
        Notification notification = new Notification();
        notification.setEventType(request.getEventType());
        notification.setUsername(request.getUsername());
        notification.setEmail(request.getEmail());
        notification.setPerformedBy(request.getPerformedBy());
        notification.setMessage(request.getMessage());
        notification.setStatus(status);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    private void sendNotificationToRecipients(NotificationRequestDto request, List<String> emails) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("Krainet Test Task <cauka008@gmail.com>");
        mailMessage.setTo(emails.toArray(new String[0]));
        mailMessage.setSubject(buildEmailSubject(request));
        mailMessage.setText(buildEmailContent(request));
        mailSender.send(mailMessage);
    }

    private String buildEmailSubject(NotificationRequestDto request) {
        String action = getActionText(request.getEventType());
        return String.format("%s пользователь %s", action, request.getUsername());
    }

    private String buildEmailContent(NotificationRequestDto request) {
        String action = getActionText(request.getEventType());
        return String.format(
                "%s пользователь с именем - %s, паролем - %s и почтой - %s.\n\n" +
                        "Выполнено: %s\n" +
                        "Тип события: %s\n\n" +
                        "Это автоматическое уведомление.",
                action,
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getPerformedBy(),
                request.getEventType()
        );
    }

    private String getActionText(String eventType) {
        return switch (eventType.toUpperCase()) {
            case "CREATED" -> "Создан";
            case "UPDATED" -> "Изменен";
            case "DELETED" -> "Удален";
            default -> "Изменен";
        };
    }

    private void handleEmptyEmails(Notification notification) {
        log.warn("No recipient emails provided for notification");
        notification.setStatus("SKIPPED");
        notification.setMessage("No recipient emails provided");
        notificationRepository.save(notification);
    }

    private void handleSuccess(Notification notification, int recipientCount) {
        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());
        notification.setRecipientCount(recipientCount);
        notificationRepository.save(notification);
    }

    private void handleFailure(Notification notification, Exception e, String username) {
        log.error("Failed to send notification email for user: {}", username, e);
        notification.setStatus("FAILED");
        notification.setMessage("Error: " + e.getMessage());
        notificationRepository.save(notification);
    }
}