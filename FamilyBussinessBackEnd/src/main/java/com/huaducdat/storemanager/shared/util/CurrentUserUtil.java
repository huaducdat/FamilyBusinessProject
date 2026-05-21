package com.huaducdat.storemanager.shared.util;

import com.huaducdat.storemanager.user.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public class CurrentUserUtil {

    public static User get(
            HttpServletRequest request
    ) {

        return (User)
                request.getAttribute(
                        "currentUser"
                );
    }
}
