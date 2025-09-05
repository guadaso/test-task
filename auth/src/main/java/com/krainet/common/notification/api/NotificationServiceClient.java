package com.krainet.common.notification.api;

import com.krainet.common.notification.dto.NotificationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "notification-service", url = "${notification.service.url:http://notification-service:8081}")
public interface NotificationServiceClient {

    @PostMapping(value = "/api/notifications/send")
    ResponseEntity<String> sendNotification(
            @RequestBody NotificationRequestDto request,
            @RequestParam("emails") List<String> emails
    );
}