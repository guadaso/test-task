package com.krainet.api.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.krainet.api.users.dto.UserDto;
import com.krainet.api.users.dto.response.AdminEmailResponse;
import com.krainet.api.users.service.UserService;
import com.krainet.common.constant.messages.LogMessage;
import com.krainet.common.logs.LogService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final LogService logService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @securityService.isOwner(#id, principal))")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logService.logAction(LogMessage.USER_GET_ATTEMPT, String.valueOf(id), true);

        try {
            UserDto userDto = userService.getUserById(id);
            logService.logAction(LogMessage.USER_GET_SUCCESS, String.valueOf(id), true);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            logService.logAction(LogMessage.USER_GET_FORBIDDEN, String.valueOf(id), false);
            throw e;
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logService.logAction(LogMessage.USERS_GET_ALL_ATTEMPT, "ALL", true);

        try {
            List<UserDto> users = userService.getAllUsers();
            logService.logAction(LogMessage.USERS_GET_ALL_SUCCESS, "ALL", true, "Retrieved " + users.size() + " users");
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logService.logAction(LogMessage.USERS_GET_ALL_FORBIDDEN, "ALL", false);
            throw e;
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @securityService.isOwner(#id, principal))")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logService.logAction(LogMessage.USER_UPDATE_ATTEMPT, String.valueOf(id), true);

        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            logService.logAction(LogMessage.USER_UPDATE_SUCCESS, String.valueOf(id), true);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            handleUpdateException(e, id, userDto);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @securityService.isOwner(#id, principal))")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logService.logAction(LogMessage.USER_DELETE_ATTEMPT, String.valueOf(id), true);

        try {
            userService.deleteUser(id);
            logService.logAction(LogMessage.USER_DELETE_SUCCESS, String.valueOf(id), true);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logService.logAction(LogMessage.USER_DELETE_FORBIDDEN, String.valueOf(id), false);
            throw e;
        }
    }

    @GetMapping("/admins/emails")
    public ResponseEntity<List<AdminEmailResponse>> getAdminEmails() {
        logService.logAction("ADMIN_EMAILS_ATTEMPT", "ADMIN_EMAILS", true, "Public request for admin emails");

        try {
            List<String> adminEmails = userService.getAdminEmails();
            List<AdminEmailResponse> response = adminEmails.stream()
                    .map(AdminEmailResponse::new)
                    .collect(Collectors.toList());

            logService.logAction("ADMIN_EMAILS_SUCCESS", "ADMIN_EMAILS", true, "Returned " + response.size() + " admin emails");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logService.logAction("ADMIN_EMAILS_FAILED", "ADMIN_EMAILS", false, "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private void handleUpdateException(RuntimeException e, Long id, UserDto userDto) {
        if (e.getMessage().contains("Username already exists")) {
            logService.logAction(LogMessage.USER_UPDATE_DUPLICATE_USERNAME, String.valueOf(id), false, "Username: " + userDto.getUserName());
        } else if (e.getMessage().contains("Email already exists")) {
            logService.logAction(LogMessage.USER_UPDATE_DUPLICATE_EMAIL, String.valueOf(id), false, "Email: " + userDto.getEmail());
        } else if (e.getMessage().contains("Only admin can change user role")) {
            logService.logAction(LogMessage.USER_UPDATE_ROLE_DENIED, String.valueOf(id), false);
        } else {
            logService.logAction(LogMessage.USER_UPDATE_FORBIDDEN, String.valueOf(id), false);
        }
    }
}