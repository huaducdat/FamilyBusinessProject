package com.huaducdat.storemanager.shared.util;

import com.huaducdat.storemanager.shared.exception.UnauthorizedException;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.user.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CurrentOwnerService {

    public Long getCurrentOwnerId() {
        return resolveCurrentOwnerId(
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

    public Store getCurrentStore() {
        return resolveCurrentStore(
                resolveRequest()
        );
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

    private Long resolveCurrentOwnerId(
            HttpServletRequest request
    ) {
        Object ownerId =
                request.getAttribute(
                        "currentOwnerId"
                );

        if (ownerId instanceof Long id) {
            return id;
        }

        Store store =
                resolveCurrentStore(
                        request
                );

        if (store == null || store.getOwner() == null) {
            User currentUser = resolveCurrentUser(request);

            if (currentUser.getStore() != null
                    && currentUser.getStore().getOwner() != null) {
                return currentUser.getStore().getOwner().getId();
            }

            throw new UnauthorizedException("Unauthorized");
        }

        return store.getOwner()
                .getId();
    }

    private Store resolveCurrentStore(
            HttpServletRequest request
    ) {
        Object currentStore =
                request.getAttribute(
                        "currentStore"
                );

        if (currentStore instanceof Store store) {
            return store;
        }

        User currentUser = resolveCurrentUser(request);

        if (currentUser.getStore() != null) {
            return currentUser.getStore();
        }

        throw new UnauthorizedException("Unauthorized");
    }

    private User resolveCurrentUser(
            HttpServletRequest request
    ) {
        Object currentUser =
                request.getAttribute(
                        "currentUser"
                );

        if (currentUser instanceof User user) {
            return user;
        }

        throw new UnauthorizedException("Unauthorized");
    }
}
