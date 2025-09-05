package com.krainet.common.constant.messages;

public class LogMessage {
    public static final String REGISTRATION_ATTEMPT = "REG_001";
    public static final String REGISTRATION_SUCCESS = "REG_002";
    public static final String REGISTRATION_DUPLICATE_USERNAME = "REG_003";
    public static final String REGISTRATION_DUPLICATE_EMAIL = "REG_004";

    public static final String LOGIN_ATTEMPT = "AUTH_001";
    public static final String LOGIN_SUCCESS = "AUTH_002";
    public static final String LOGIN_FAILED = "AUTH_003";

    public static final String USER_GET_ATTEMPT = "USER_GET_001";
    public static final String USER_GET_SUCCESS = "USER_GET_002";
    public static final String USER_GET_FORBIDDEN = "USER_GET_003";

    public static final String USERS_GET_ALL_ATTEMPT = "USERS_GET_ALL_001";
    public static final String USERS_GET_ALL_SUCCESS = "USERS_GET_ALL_002";
    public static final String USERS_GET_ALL_FORBIDDEN = "USERS_GET_ALL_003";

    public static final String USER_UPDATE_ATTEMPT = "USER_UPDATE_001";
    public static final String USER_UPDATE_SUCCESS = "USER_UPDATE_002";
    public static final String USER_UPDATE_FORBIDDEN = "USER_UPDATE_003";
    public static final String USER_UPDATE_DUPLICATE_USERNAME = "USER_UPDATE_004";
    public static final String USER_UPDATE_DUPLICATE_EMAIL = "USER_UPDATE_005";
    public static final String USER_UPDATE_ROLE_DENIED = "USER_UPDATE_006";

    public static final String USER_DELETE_ATTEMPT = "USER_DELETE_001";
    public static final String USER_DELETE_SUCCESS = "USER_DELETE_002";
    public static final String USER_DELETE_FORBIDDEN = "USER_DELETE_003";

    public static final String ACCESS_DENIED = "ACCESS_001";

    public static final String NOTIFICATION_SEND_ATTEMPT = "NOTIFICATION_001";
    public static final String NOTIFICATION_SEND_SUCCESS = "NOTIFICATION_002";
    public static final String NOTIFICATION_SEND_FAILED = "NOTIFICATION_003";
    public static final String NOTIFICATION_PREPARE_FAILED = "NOTIFICATION_004";

    public static final String USERS_GET_ADMINS_ATTEMPT = "USERS_GET_ADMINS_001";
    public static final String USERS_GET_ADMINS_SUCCESS = "USERS_GET_ADMINS_002";
    public static final String USERS_GET_ADMINS_FORBIDDEN = "USERS_GET_ADMINS_003";
    public static final String USERS_GET_ADMINS_NOT_FOUND = "USERS_GET_ADMINS_004";

    public static String getMessage(String code) {
        return switch (code) {
            case REGISTRATION_ATTEMPT -> "Registration attempt";
            case REGISTRATION_SUCCESS -> "Registration successful";
            case REGISTRATION_DUPLICATE_USERNAME -> "Registration failed - duplicate username";
            case REGISTRATION_DUPLICATE_EMAIL -> "Registration failed - duplicate email";
            case LOGIN_ATTEMPT -> "Login attempt";
            case LOGIN_SUCCESS -> "Login successful";
            case LOGIN_FAILED -> "Login failed";
            case USER_GET_ATTEMPT -> "User data retrieval attempt";
            case USER_GET_SUCCESS -> "User data retrieval successful";
            case USER_GET_FORBIDDEN -> "User data retrieval forbidden";
            case USERS_GET_ALL_ATTEMPT -> "All users data retrieval attempt";
            case USERS_GET_ALL_SUCCESS -> "All users data retrieval successful";
            case USERS_GET_ALL_FORBIDDEN -> "All users data retrieval forbidden";
            case USER_UPDATE_ATTEMPT -> "User data update attempt";
            case USER_UPDATE_SUCCESS -> "User data update successful";
            case USER_UPDATE_FORBIDDEN -> "User data update forbidden";
            case USER_UPDATE_DUPLICATE_USERNAME -> "User data update failed - duplicate username";
            case USER_UPDATE_DUPLICATE_EMAIL -> "User data update failed - duplicate email";
            case USER_UPDATE_ROLE_DENIED -> "User data update failed - role change denied";
            case USER_DELETE_ATTEMPT -> "User deletion attempt";
            case USER_DELETE_SUCCESS -> "User deletion successful";
            case USER_DELETE_FORBIDDEN -> "User deletion forbidden";
            case ACCESS_DENIED -> "Access denied";
            case NOTIFICATION_SEND_ATTEMPT -> "Notification sending attempt";
            case NOTIFICATION_SEND_SUCCESS -> "Notification sent successfully";
            case NOTIFICATION_SEND_FAILED -> "Notification sending failed";
            case NOTIFICATION_PREPARE_FAILED -> "Notification preparation failed";
            case USERS_GET_ADMINS_ATTEMPT -> "Admin users data retrieval attempt";
            case USERS_GET_ADMINS_SUCCESS -> "Admin users data retrieval successful";
            case USERS_GET_ADMINS_FORBIDDEN -> "Admin users data retrieval forbidden";
            case USERS_GET_ADMINS_NOT_FOUND -> "Admin users not found";
            default -> "Unknown operation";
        };
    }
}