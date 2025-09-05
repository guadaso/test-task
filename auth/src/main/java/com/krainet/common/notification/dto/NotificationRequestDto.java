package com.krainet.common.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private String eventType;
    private String username;
    private String password;
    private String email;
    private String performedBy;
    private String message;
}