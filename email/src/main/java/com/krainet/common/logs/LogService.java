package com.krainet.common.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class LogService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logAction(String actionCode, String target, boolean success, String additionalInfo) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String status = success ? "SUCCESS" : "FAILED";
        String fullInfo = additionalInfo.isEmpty() ? "" : additionalInfo + " | Status Code: " + (success ? "200" : "500");

        log.info("[{}] [NOTIFICATION_ACTION] [{}] Action: {} | Target: {} | Status: {} | Info: {}",
                timestamp, actionCode, getActionMessage(actionCode), target, status, fullInfo);
    }

    public void logAction(String actionCode, String target, boolean success) {
        logAction(actionCode, target, success, "");
    }

    public void logNotificationAttempt(String eventType, String username) {
        log.info("[{}] [NOTIFICATION_ATTEMPT] [{}] Notification attempt for user: {}",
                LocalDateTime.now().format(FORMATTER), eventType, username);
    }

    public void logNotificationSuccess(String eventType, String username, int recipientCount) {
        log.info("[{}] [NOTIFICATION_SUCCESS] [{}] Notification sent successfully for user: {} to {} recipients",
                LocalDateTime.now().format(FORMATTER), eventType, username, recipientCount);
    }

    public void logNotificationFailure(String eventType, String username, String error) {
        log.info("[{}] [NOTIFICATION_FAILED] [{}] Notification failed for user: {} | Error: {}",
                LocalDateTime.now().format(FORMATTER), eventType, username, error);
    }

    private String getActionMessage(String actionCode) {
        return switch (actionCode) {
            case "NOTIFICATION_SEND_ATTEMPT" -> "Notification send attempt";
            case "NOTIFICATION_SEND_SUCCESS" -> "Notification sent successfully";
            case "NOTIFICATION_SEND_FAILED" -> "Notification send failed";
            case "NOTIFICATION_GET_ALL_ATTEMPT" -> "Get all notifications attempt";
            case "NOTIFICATION_GET_ALL_SUCCESS" -> "Get all notifications success";
            case "NOTIFICATION_GET_ALL_FAILED" -> "Get all notifications failed";
            default -> actionCode;
        };
    }
}