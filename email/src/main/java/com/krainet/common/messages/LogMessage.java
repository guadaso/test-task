package com.krainet.common.messages;

public class LogMessage {

    public static final String NOTIFICATION_SEND_ATTEMPT = "NOTIF_001";
    public static final String NOTIFICATION_SEND_SUCCESS = "NOTIF_002";
    public static final String NOTIFICATION_SEND_FAILED = "NOTIF_003";
    public static final String NOTIFICATION_GET_ALL_ATTEMPT = "NOTIF_004";
    public static final String NOTIFICATION_GET_ALL_SUCCESS = "NOTIF_005";
    public static final String NOTIFICATION_GET_ALL_FAILED = "NOTIF_006";
    public static final String NOTIFICATION_EMPTY_EMAILS = "NOTIF_007";
    public static final String NOTIFICATION_PROCESSING_START = "NOTIF_008";
    public static final String NOTIFICATION_PROCESSING_END = "NOTIF_009";

    public static String getMessage(String code) {
        return switch (code) {
            case NOTIFICATION_SEND_ATTEMPT -> "Notification sending attempt";
            case NOTIFICATION_SEND_SUCCESS -> "Notification sent successfully";
            case NOTIFICATION_SEND_FAILED -> "Notification sending failed";
            case NOTIFICATION_GET_ALL_ATTEMPT -> "Get all notifications attempt";
            case NOTIFICATION_GET_ALL_SUCCESS -> "Get all notifications successful";
            case NOTIFICATION_GET_ALL_FAILED -> "Get all notifications failed";
            case NOTIFICATION_EMPTY_EMAILS -> "No recipient emails provided";
            case NOTIFICATION_PROCESSING_START -> "Notification processing started";
            case NOTIFICATION_PROCESSING_END -> "Notification processing completed";
            default -> "Unknown notification operation";
        };
    }
}