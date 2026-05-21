package com.huaducdat.storemanager.shared.security;

import com.huaducdat.storemanager.shared.exception.UnauthorizedException;

public final class BearerTokenUtil {

    private BearerTokenUtil() {
    }

    public static String extractBearerToken(
            String authorizationHeader
    ) {

        if (authorizationHeader == null
                || authorizationHeader.isBlank()) {

            throw new UnauthorizedException(
                    "Missing token"
            );
        }

        if (!authorizationHeader.startsWith("Bearer ")) {

            throw new UnauthorizedException(
                    "Malformed Authorization header"
            );
        }

        String token =
                authorizationHeader
                        .substring(7)
                        .trim();

        if (token.isEmpty()) {

            throw new UnauthorizedException(
                    "Missing token"
            );
        }

        return token;
    }
}
