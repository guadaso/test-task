package com.krainet.api.users.service.validation;

import com.krainet.api.users.entity.Users;
import com.krainet.api.users.repository.UserRepository;
import com.krainet.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateCreateUser(String username, String email) {
        if (userRepository.findByUserName(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
    }

    public void validateUpdateUser(Long userId, String newUsername, String newEmail) {
        if (newUsername != null && !isUsernameUnique(newUsername, userId)) {
            throw new RuntimeException("Username already exists");
        }
        if (newEmail != null && !isEmailUnique(newEmail, userId)) {
            throw new RuntimeException("Email already exists");
        }
    }

    public void validateRoleChange(UserRole currentUserRole, UserRole newRole) {
        if (newRole != null && !UserRole.ADMIN.equals(currentUserRole)) {
            throw new RuntimeException("Only admin can change user role");
        }
    }

    private boolean isUsernameUnique(String username, Long excludeUserId) {
        return userRepository.findByUserName(username)
                .map(user -> user.getId().equals(excludeUserId))
                .orElse(true);
    }

    private boolean isEmailUnique(String email, Long excludeUserId) {
        return userRepository.findByEmail(email)
                .map(user -> user.getId().equals(excludeUserId))
                .orElse(true);
    }
}