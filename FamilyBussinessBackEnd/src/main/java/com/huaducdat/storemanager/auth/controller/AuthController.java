package com.huaducdat.storemanager.auth.controller;

import com.huaducdat.storemanager.auth.dto.request.CreateOwnerRequest;
import com.huaducdat.storemanager.auth.dto.request.LoginRequest;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.auth.dto.response.CurrentUserResponse;
import com.huaducdat.storemanager.auth.dto.response.LoginResponse;
import com.huaducdat.storemanager.auth.service.AuthService;
import com.huaducdat.storemanager.shared.security.BearerTokenUtil;
import com.huaducdat.storemanager.shared.util.CurrentOwnerService;
import com.huaducdat.storemanager.shared.util.CurrentUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final CurrentUserService currentUserService;

    private final CurrentOwnerService currentOwnerService;

    public AuthController(
            AuthService authService,
            CurrentUserService currentUserService,
            CurrentOwnerService currentOwnerService
    ) {

        this.authService = authService;
        this.currentUserService = currentUserService;
        this.currentOwnerService = currentOwnerService;
    }

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        LoginResponse response =
                authService.login(request);

        return BaseResponse.success(
                "Login success",
                response
        );
    }

    @GetMapping("/me")
    public BaseResponse<CurrentUserResponse> me(
    ) {

        var user =
                currentUserService.getCurrentUser();
        var store =
                currentUserService.getCurrentStore();

        CurrentUserResponse response =
                CurrentUserResponse.builder()
                        .id(user.getId())
                        .userId(user.getId())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .userRole(user.getUserRole())
                        .ownerId(currentOwnerService.getCurrentOwnerId())
                        .storeId(currentUserService.getCurrentStoreId())
                        .storeName(store != null ? store.getName() : null)
                        .active(
                                user.getActive()
                        )
                        .build();

        return BaseResponse.success(
                "Current user",
                response
        );
    }

    @PostMapping("/logout")
    public BaseResponse<?> logout(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authorizationHeader
    ) {

        String token =
                BearerTokenUtil.extractBearerToken(
                        authorizationHeader
                );

        authService.logout(token);

        return BaseResponse.success(
                "Logout success",
                null
        );
    }

    @PostMapping("/bootstrap-owner")
    public BaseResponse<?> bootstrapOwner(
            @RequestBody CreateOwnerRequest body
    ) {

        authService.createOwner(
                body
        );

        return BaseResponse.success(
                "Owner created",
                null
        );
    }
}
