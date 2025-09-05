package com.krainet.api.notification.controller;

import com.krainet.api.notification.dto.response.NotificationResponseDto;
import com.krainet.common.messages.LogMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.krainet.api.notification.dto.request.NotificationRequestDto;
import com.krainet.api.notification.service.NotificationService;
import com.krainet.common.messages.LogMessage;
import com.krainet.common.logs.LogService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final LogService logService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestBody NotificationRequestDto request,
            @RequestParam("emails") List<String> emails) {

        logService.logAction(LogMessage.NOTIFICATION_SEND_ATTEMPT, request.getUsername(), true);

        try {
            notificationService.sendNotification(request, emails);
            logService.logAction(LogMessage.NOTIFICATION_SEND_SUCCESS, request.getUsername(), true,
                    "Sent to " + emails.size() + " recipients");
            return ResponseEntity.ok("Notification sent successfully");
        } catch (Exception e) {
            logService.logAction(LogMessage.NOTIFICATION_SEND_FAILED, request.getUsername(), false,
                    "Error: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Failed to send notification: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getAllNotifications() {
        logService.logAction(LogMessage.NOTIFICATION_GET_ALL_ATTEMPT, "ALL", true);

        try {
            List<NotificationResponseDto> notifications = notificationService.getAllNotifications();
            logService.logAction(LogMessage.NOTIFICATION_GET_ALL_SUCCESS, "ALL", true,
                    "Retrieved " + notifications.size() + " notifications");
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            logService.logAction(LogMessage.NOTIFICATION_GET_ALL_FAILED, "ALL", false,
                    "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}