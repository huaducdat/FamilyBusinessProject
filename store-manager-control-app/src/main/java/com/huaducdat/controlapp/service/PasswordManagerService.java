package com.huaducdat.controlapp.service;

import com.huaducdat.controlapp.util.ConfigUtil;
import com.huaducdat.controlapp.util.PasswordHashUtil;

public class PasswordManagerService {

    // =========================
    // SETUP
    // =========================

    public void setupPassword(
            String password
    ) {

        String hash =

                PasswordHashUtil.hash(
                        password
                );

        ConfigUtil.save(
                "technical.password.hash",
                hash
        );
    }

    // =========================
    // CHANGE
    // =========================

    public boolean changePassword(
            String oldPassword,
            String newPassword
    ) {

        String currentHash =

                ConfigUtil.get(
                        "technical.password.hash"
                );

        boolean valid =

                PasswordHashUtil.matches(
                        oldPassword,
                        currentHash
                );

        if (!valid) {

            return false;
        }

        String newHash =

                PasswordHashUtil.hash(
                        newPassword
                );

        ConfigUtil.save(
                "technical.password.hash",
                newHash
        );

        return true;
    }
}