package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.ChangePasswordRequest;
import com.huaducdat.storemanager.model.request.CreateUserRequest;
import com.huaducdat.storemanager.model.request.ToggleUserActiveRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.service.user.UserService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService
    ) {

        this.userService = userService;
    }

    @PostMapping
    public BaseResponse<?> create(
            HttpServletRequest request,
            @RequestBody CreateUserRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        User user =
                userService.create(
                        currentUser,
                        body
                );

        return BaseResponse.builder()
                .success(true)
                .message("User created")
                .data(user.getUsername())
                .build();
    }

    @GetMapping
    public BaseResponse<?> list(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("User list")
                .data(
                        userService.list(
                                currentUser
                        )
                )
                .build();
    }


    @PatchMapping("/{id}/active")
    public BaseResponse<?> toggleActive(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody ToggleUserActiveRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        userService.toggleActive(
                currentUser,
                id,
                body.getActive()
        );

        return BaseResponse.builder()
                .success(true)
                .message("User updated")
                .data(null)
                .build();
    }

    @PatchMapping("/password")
    public BaseResponse<?> changePassword(
            HttpServletRequest request,
            @RequestBody ChangePasswordRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        userService.changePassword(
                currentUser,
                body
        );

        return BaseResponse.builder()
                .success(true)
                .message("Password changed")
                .data(null)
                .build();
    }
}