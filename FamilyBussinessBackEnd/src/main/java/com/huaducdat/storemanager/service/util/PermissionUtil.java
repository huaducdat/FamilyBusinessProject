package com.huaducdat.storemanager.service.util;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.UserRole;

public class PermissionUtil {

    // =========================
    // ADMIN ONLY
    // =========================

    public static void requireAdmin(
            User user
    ) {

        if (user.getUserRole()
                != UserRole.ADMIN) {

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

        if (
                user.getUserRole() != UserRole.OWNER
                        &&
                        user.getUserRole() != UserRole.ADMIN
                        &&
                        user.getUserRole() != UserRole.MANAGER
        ){

            throw new RuntimeException(
                    "Permission denied"
            );
        }
    }

    // =========================
    // ROLE LEVEL
    // =========================

    public static int level(
            UserRole userRole
    ) {

        return switch (userRole) {

            case ADMIN -> 3;

            case MANAGER -> 2;

            case EMPLOYEE -> 1;

            case OWNER -> 0;
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
                        currentUser.getUserRole()
                );

        int targetLevel =
                level(
                        target.getUserRole()
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
                user.getUserRole() != UserRole.OWNER
                        &&
                        user.getUserRole() != UserRole.ADMIN
                        &&
                        user.getUserRole() != UserRole.MANAGER
                        &&
                        user.getUserRole() != UserRole.EMPLOYEE
        ) {

            throw new RuntimeException(
                    "Permission denied"
            );
        }
    }
}