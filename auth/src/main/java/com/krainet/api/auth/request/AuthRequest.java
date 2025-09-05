package com.krainet.api.auth.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}