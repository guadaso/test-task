package com.krainet.api.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String eventType;
    private String username;
    private String email;
    private String performedBy;
    private String message;
    private String status;
    private Integer recipientCount;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}