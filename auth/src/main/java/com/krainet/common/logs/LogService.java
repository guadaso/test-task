package com.krainet.common.logs;

import com.krainet.common.constant.messages.LogMessage;
import com.krainet.common.model.entity.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class LogService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logAction(String actionCode, String targetUserId, boolean success, String additionalInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String currentUser = "anonymous";
        String currentUserId = "N/A";

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            currentUser = auth.getName();
            if (auth.getPrincipal() instanceof CustomUserDetails) {
                currentUserId = String.valueOf(((CustomUserDetails) auth.getPrincipal()).getId());
            }
        }

        String status = success ? "SUCCESS" : "FAILED";
        String message = LogMessage.getMessage(actionCode);
        String fullInfo = additionalInfo.isEmpty() ? "" : additionalInfo + " | Status Code: " + (success ? "200" : "403");
        log.info("[{}] [USER_ACTION] [{}] User: {} (ID: {}) | Action: {} | Target: {} |Status: {} | Info: {}",
                timestamp, actionCode, currentUser, currentUserId, message, targetUserId, status, fullInfo);
    }

    public void logAction(String actionCode, String targetUserId, boolean success) {
        logAction(actionCode, targetUserId, success, "");
    }

    public void logRegistrationAttempt(String username) {
        log.info("[{}] [{}] [REGISTRATION}Anonymous user attempting to register withusername: {}",
                LocalDateTime.now().format(FORMATTER), LogMessage.REGISTRATION_ATTEMPT, username);
    }

    public void logRegistrationSuccess(String username, Long userId) {
        log.info("[{}] [{}] [REGISTRATION}User {} registered successfully with ID: {}",
                LocalDateTime.now().format(FORMATTER), LogMessage.REGISTRATION_SUCCESS, username, userId);
    }

    public void logLoginAttempt(String username) {
        log.info("[{}] [{}] [LOGIN}Login attempt for user: {}",
                LocalDateTime.now().format(FORMATTER), LogMessage.LOGIN_ATTEMPT, username);
    }

    public void logLoginSuccess(String username, Long userId) {
        log.info("[{}] [{}] [LOGIN}User {} (ID: {}) logged insuccessfully",
                LocalDateTime.now().format(FORMATTER), LogMessage.LOGIN_SUCCESS, username, userId);
    }
}