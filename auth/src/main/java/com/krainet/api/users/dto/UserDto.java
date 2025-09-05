package com.krainet.api.users.dto;

import com.krainet.common.enums.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
}