package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.CreateOwnerRequest;
import com.huaducdat.storemanager.model.request.LoginRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.model.response.LoginResponse;
import com.huaducdat.storemanager.service.auth.AuthService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {

        this.authService = authService;
    }

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        LoginResponse response =
                authService.login(request);

        return BaseResponse
                .<LoginResponse>builder()
                .success(true)
                .message("Login success")
                .data(response)
                .build();
    }

    @GetMapping("/me")
    public BaseResponse<?> me(
            HttpServletRequest request
    ) {

        User user =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Current user")
                .data(user.getUsername())
                .build();
    }

    @PostMapping("/logout")
    public BaseResponse<?> logout(
            HttpServletRequest request
    ) {

        String authHeader =
                request.getHeader(
                        "Authorization"
                );

        String token =
                authHeader.substring(7);

        authService.logout(token);

        return BaseResponse.builder()
                .success(true)
                .message("Logout success")
                .data(null)
                .build();
    }

    @PostMapping("/bootstrap-owner")
    public BaseResponse<?> bootstrapOwner(
            @RequestBody CreateOwnerRequest body
    ) {

        authService.createOwner(
                body
        );

        return BaseResponse.builder()
                .success(true)
                .message("Owner created")
                .data(null)
                .build();
    }
}