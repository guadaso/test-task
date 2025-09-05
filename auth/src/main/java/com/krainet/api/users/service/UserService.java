package com.krainet.api.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.krainet.api.users.dto.UserDto;
import com.krainet.api.users.entity.Users;
import com.krainet.api.users.mapper.UserMapper;
import com.krainet.api.users.repository.UserRepository;
import com.krainet.api.users.service.notification.UserNotificationService;
import com.krainet.api.users.service.security.SecurityContextService;
import com.krainet.api.users.service.validation.UserValidator;
import com.krainet.common.enums.UserRole;
import com.krainet.common.model.entity.CustomUserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final SecurityContextService securityContextService;
    private final UserNotificationService userNotificationService;

    @Transactional
    public UserDto createUser(UserDto userDto) {
        userValidator.validateCreateUser(userDto.getUserName(), userDto.getEmail());

        Users user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Users savedUser = userRepository.save(user);

        if (UserRole.ADMIN.equals(savedUser.getRole())) {
            sendNotification("CREATED", savedUser, userDto.getPassword());
        }

        return userMapper.toDto(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        return userMapper.toDto(getUserEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        Users existingUser = getUserEntityById(id);
        CustomUserDetails currentUser = securityContextService.getCurrentUser();

        userValidator.validateRoleChange(currentUser.getRole(), userDto.getRole());
        userValidator.validateUpdateUser(id, userDto.getUserName(), userDto.getEmail());

        UserRole oldRole = existingUser.getRole();
        updateUserFields(existingUser, userDto, currentUser);
        Users updatedUser = userRepository.save(existingUser);

        handleAdminNotifications(oldRole, updatedUser.getRole(), updatedUser, userDto);

        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        Users user = getUserEntityById(id);
        boolean isAdmin = UserRole.ADMIN.equals(user.getRole());

        userRepository.deleteById(id);

        if (isAdmin) {
            sendNotification("DELETED", user, "***");
        }
    }

    @Transactional(readOnly = true)
    public List<String> getAdminEmails() {
        return userRepository.findByRole(UserRole.ADMIN).stream()
                .map(Users::getEmail)
                .filter(email -> email != null && !email.trim().isEmpty())
                .collect(Collectors.toList());
    }

    private Users getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    private void updateUserFields(Users existingUser, UserDto userDto, CustomUserDetails currentUser) {
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userMapper.updateEntity(userDto, existingUser);

        if (userDto.getRole() != null && UserRole.ADMIN.equals(currentUser.getRole())) {
            existingUser.setRole(userDto.getRole());
        }
    }

    private void handleAdminNotifications(UserRole oldRole, UserRole newRole, Users updatedUser, UserDto userDto) {
        boolean wasAdmin = UserRole.ADMIN.equals(oldRole);
        boolean isAdminNow = UserRole.ADMIN.equals(newRole);

        if (wasAdmin || isAdminNow) {
            String passwordToSend = userDto.getPassword() != null ? userDto.getPassword() : "***";
            sendNotification("UPDATED", updatedUser, passwordToSend);
        }
    }

    private void sendNotification(String eventType, Users user, String password) {
        if (!UserRole.ADMIN.equals(user.getRole())) {
            return;
        }

        String username = user.getUserName();
        String email = user.getEmail();
        String performedBy = securityContextService.getCurrentUserInfo();
        List<String> adminEmails = getAdminEmails();

        userNotificationService.sendNotificationAfterCommit(
                eventType, username, password, email, performedBy, adminEmails
        );
    }
}