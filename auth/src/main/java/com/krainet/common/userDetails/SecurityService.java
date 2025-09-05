package com.krainet.common.userDetails;

import com.krainet.common.model.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("securityService")
@RequiredArgsConstructor
public class SecurityService {

    public boolean isOwner(Long userId, Object principal) {
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return userDetails.getId().equals(userId);
        }
        return false;
    }
}