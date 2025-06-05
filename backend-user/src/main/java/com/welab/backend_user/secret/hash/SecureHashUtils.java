package com.welab.backend_user.secret.hash;

public class SecureHashUtils {
    public static String hash(String message) {
        // TODO: message -> SHA-1 or SHA-256 해시 값으로 변환

        return message;
    }

    public static boolean matches(String message, String hashedMessage) {
        String hashed = hash(message);

        return hashed.equals(hashedMessage);
    }
}
