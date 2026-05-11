package com.huaducdat.storemanager.service.util;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.Role;

public class PermissionUtil {

    // =========================
    // ADMIN ONLY
    // =========================

    public static void requireAdmin(
            User user
    ) {

        if (user.getRole()
                != Role.ADMIN) {

            throw new RuntimeException(
                    "Permission denied"
            );
        }
    }

    // =========================
    // MANAGER+
    // =========================

    public static void requireManager(
            User user
    ) {

        if (user.getRole() == Role.EMPLOYEE) {

            throw new RuntimeException(
                    "Permission denied"
            );
        }
    }

    // =========================
    // ROLE LEVEL
    // =========================

    public static int level(
            Role role
    ) {

        return switch (role) {

            case ADMIN -> 3;

            case MANAGER -> 2;

            case EMPLOYEE -> 1;
        };
    }

    // =========================
    // CAN MODIFY TARGET
    // =========================

    public static void requireHigher(
            User currentUser,
            User target
    ) {

        int current =
                level(
                        currentUser.getRole()
                );

        int targetLevel =
                level(
                        target.getRole()
                );

        if (current <= targetLevel) {

            throw new RuntimeException(
                    "Cannot modify this user"
            );
        }
    }

    public static void requireEmployee(
            User user
    ) {

        if (user == null) {

            throw new RuntimeException(
                    "Unauthorized"
            );
        }

        if (
                user.getRole() != Role.ADMIN
                        &&
                        user.getRole() != Role.MANAGER
                        &&
                        user.getRole() != Role.EMPLOYEE
        ) {

            throw new RuntimeException(
                    "Permission denied"
            );
        }
    }
}