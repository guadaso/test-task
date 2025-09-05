package com.krainet.api.notification.entity;

import com.krainet.common.base.TimeStampedEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification extends TimeStampedEntity<Long> {

    @Column(name = "event_type", nullable = false, length = 20)
    private String eventType;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "performed_by", length = 100)
    private String performedBy;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "recipient_count")
    private Integer recipientCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}