package com.huaducdat.storemanager.shared.util;

import com.huaducdat.storemanager.shared.exception.UnauthorizedException;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.store.model.entity.Store;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CurrentUserService {

    public User getCurrentUser() {
        return resolveCurrentUser(
                resolveRequest()
        );
    }

    public User getCurrentUser(
            HttpServletRequest request
    ) {
        return resolveCurrentUser(
                request
        );
    }

    public Store getCurrentStore() {
        return getCurrentStore(
                resolveRequest()
        );
    }

    public Long getCurrentStoreId() {
        Store store =
                getCurrentStore();

        return store != null
                ? store.getId()
                : null;
    }

    public Long getCurrentOwnerId() {
        Store store = getCurrentStore();

        return store != null && store.getOwner() != null
                ? store.getOwner().getId()
                : null;
    }

    private HttpServletRequest resolveRequest() {
        RequestAttributes attributes =
                RequestContextHolder.getRequestAttributes();

        if (!(attributes instanceof ServletRequestAttributes servletRequestAttributes)) {

            throw new UnauthorizedException(
                    "Unauthorized"
            );
        }

        return servletRequestAttributes.getRequest();
    }

    private User resolveCurrentUser(
            HttpServletRequest request
    ) {
        Object currentUser =
                request.getAttribute(
                        "currentUser"
                );

        if (!(currentUser instanceof User user)) {

            throw new UnauthorizedException(
                    "Unauthorized"
            );
        }

        return user;
    }

    private Store getCurrentStore(
            HttpServletRequest request
    ) {
        Object currentStore =
                request.getAttribute(
                        "currentStore"
                );

        if (currentStore instanceof Store store) {
            return store;
        }

        User currentUser =
                resolveCurrentUser(
                        request
                );

        return currentUser.getStore();
    }
}
