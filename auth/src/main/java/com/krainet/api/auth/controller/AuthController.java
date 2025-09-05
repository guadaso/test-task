package com.krainet.api.auth.controller;

import com.krainet.api.auth.request.AuthRequest;
import com.krainet.api.auth.request.RegisterRequest;
import com.krainet.api.users.dto.UserDto;
import com.krainet.api.users.service.UserService;
import com.krainet.common.constant.messages.LogMessage;
import com.krainet.common.enums.UserRole;
import com.krainet.common.logs.LogService;
import com.krainet.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final LogService logService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        logService.logLoginAttempt(request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            String token = jwtTokenProvider.createToken((UserDetails) authentication.getPrincipal());
            if (authentication.getPrincipal() instanceof com.krainet.common.model.entity.CustomUserDetails) {
                Long userId = ((com.krainet.common.model.entity.CustomUserDetails) authentication.getPrincipal()).getId();
                logService.logLoginSuccess(request.getUsername(), userId);
            }

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            logService.logAction(LogMessage.LOGIN_FAILED, "N/A", false,
                    "Failed login attempt for user: " + request.getUsername());
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest registerRequest) {
        logService.logRegistrationAttempt(registerRequest.getUsername());

        try {
            String role = registerRequest.getRole();
            if (role == null || role.trim().isEmpty()) {
                role = "USER";
            }

            UserDto userDto = UserDto.builder()
                    .userName(registerRequest.getUsername())
                    .password(registerRequest.getPassword())
                    .email(registerRequest.getEmail())
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .role(UserRole.valueOf(role))
                    .build();
            UserDto createdUser = userService.createUser(userDto);
            logService.logRegistrationSuccess(createdUser.getUserName(), createdUser.getId());
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Username already exists")) {
                logService.logAction(LogMessage.REGISTRATION_DUPLICATE_USERNAME, "N/A", false,
                        "Username: " + registerRequest.getUsername());
            } else if (e.getMessage().contains("Email already exists")) {
                logService.logAction(LogMessage.REGISTRATION_DUPLICATE_EMAIL, "N/A", false,
                        "Email: " + registerRequest.getEmail());
            }
            throw e;
        }
    }
}