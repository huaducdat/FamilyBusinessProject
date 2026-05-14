package com.huaducdat.controlapp.service;

import com.huaducdat.controlapp.util.ConfigUtil;
import com.huaducdat.controlapp.util.PasswordHashUtil;

public class TechnicalAuthService {

    // =========================
    // HAS PASSWORD
    // =========================

    public boolean hasPassword() {

        String hash =

                ConfigUtil.get(
                        "technical.password.hash"
                );

        return hash != null
                &&
                !hash.isBlank();
    }

    // =========================
    // VALIDATE
    // =========================

    public boolean validate(
            String password
    ) {

        String hash =

                ConfigUtil.get(
                        "technical.password.hash"
                );

        return PasswordHashUtil.matches(
                password,
                hash
        );
    }
}