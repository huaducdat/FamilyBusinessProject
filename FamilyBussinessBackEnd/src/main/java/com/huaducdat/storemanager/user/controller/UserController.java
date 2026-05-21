package com.huaducdat.storemanager.user.controller;

import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.user.dto.request.ChangePasswordRequest;
import com.huaducdat.storemanager.user.dto.request.CreateUserRequest;
import com.huaducdat.storemanager.user.dto.request.ToggleUserActiveRequest;
import com.huaducdat.storemanager.user.dto.request.UpdateUserStoreRequest;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.user.service.UserService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
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

    @PatchMapping("/{id}/store")
    public BaseResponse<?> transferStore(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody UpdateUserStoreRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        userService.transferStore(
                currentUser,
                id,
                body.getStoreId()
        );

        return BaseResponse.builder()
                .success(true)
                .message("User transferred")
                .data(null)
                .build();
    }
}
