package com.huaducdat.controlapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashUtil {

    private static final BCryptPasswordEncoder
            encoder =
            new BCryptPasswordEncoder();

    // =========================
    // HASH
    // =========================

    public static String hash(
            String password
    ) {

        return encoder.encode(
                password
        );
    }

    // =========================
    // MATCH
    // =========================

    public static boolean matches(
            String rawPassword,
            String hashedPassword
    ) {

        return encoder.matches(
                rawPassword,
                hashedPassword
        );
    }
}