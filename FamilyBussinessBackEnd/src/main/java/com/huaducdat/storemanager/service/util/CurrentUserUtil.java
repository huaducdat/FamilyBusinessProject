package com.huaducdat.storemanager.service.util;

import com.huaducdat.storemanager.model.entity.User;
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