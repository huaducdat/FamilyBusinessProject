package com.huaducdat.storemanager.service.user;

import com.huaducdat.storemanager.model.entity.Store;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.Role;
import com.huaducdat.storemanager.model.request.ChangePasswordRequest;
import com.huaducdat.storemanager.model.request.CreateUserRequest;
import com.huaducdat.storemanager.model.request.UpdateUserRequest;
import com.huaducdat.storemanager.model.response.UserResponse;
import com.huaducdat.storemanager.repository.UserRepository;
import com.huaducdat.storemanager.service.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder encoder
    ) {

        this.userRepository = userRepository;

        this.encoder = encoder;
    }

    // =========================
    // CREATE USER
    // =========================

    public User create(
            User currentUser,
            CreateUserRequest request
    ) {

        // =========================
        // PERMISSION
        // =========================

        PermissionUtil.requireManager(
                currentUser
        );

        // =========================
        // EXIST USERNAME
        // =========================

        boolean existed =
                userRepository
                        .findByUsername(
                                request.getUsername()
                        )
                        .isPresent();

        if (existed) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        // =========================
        // MANAGER LIMIT
        // =========================

        if (currentUser.getRole()
                == Role.MANAGER
                && request.getRole()
                != Role.EMPLOYEE) {

            throw new RuntimeException(
                    "Manager can only create employee"
            );
        }

        // =========================
        // STORE
        // =========================

        Store store =
                currentUser.getStore();

        // =========================
        // CREATE USER
        // =========================

        User user = new User();

        user.setStore(store);

        user.setUsername(
                request.getUsername()
        );

        user.setPassword(
                encoder.encode(
                        request.getPassword()
                )
        );

        user.setFullName(
                request.getFullName()
        );

        user.setPhone(
                request.getPhone()
        );

        user.setRole(
                request.getRole()
        );

        user.setActive(true);

        return userRepository.save(user);
    }

    // =========================
    // LIST USER
    // =========================

    public List<UserResponse> list(
            User currentUser
    ) {

        // =========================
        // PERMISSION
        // =========================

        PermissionUtil.requireManager(
                currentUser
        );

        return userRepository
                .findAll()
                .stream()

                // =========================
                // FILTER BY ROLE
                // =========================

                .filter(user -> {

                    if (currentUser.getRole()
                            == Role.ADMIN) {

                        return true;
                    }

                    return user.getRole()
                            == Role.EMPLOYEE;
                })

                // =========================
                // MAP RESPONSE
                // =========================

                .map(user ->
                        UserResponse.builder()
                                .id(user.getId())
                                .username(
                                        user.getUsername()
                                )
                                .fullName(
                                        user.getFullName()
                                )
                                .phone(
                                        user.getPhone()
                                )
                                .role(
                                        user.getRole()
                                )
                                .active(
                                        user.getActive()
                                )
                                .build()
                )
                .toList();
    }

    // =========================
    // TOGGLE ACTIVE
    // =========================

    public void toggleActive(
            User currentUser,
            Long userId,
            Boolean active
    ) {

        // =========================
        // PERMISSION
        // =========================

        PermissionUtil.requireManager(
                currentUser
        );

        // =========================
        // FIND USER
        // =========================

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        // =========================
        // HIERARCHY
        // =========================

        PermissionUtil.requireHigher(
                currentUser,
                user
        );

        // =========================
        // CANNOT DISABLE SELF
        // =========================

        if (user.getId().equals(
                currentUser.getId()
        )) {

            throw new RuntimeException(
                    "Cannot disable yourself"
            );
        }

        // =========================
        // UPDATE
        // =========================

        user.setActive(active);

        userRepository.save(user);
    }

    // =========================
    // CHANGE PASSWORD
    // =========================

    public void changePassword(
            User currentUser,
            ChangePasswordRequest request
    ) {

        // =========================
        // CHECK OLD PASSWORD
        // =========================

        boolean matched =
                encoder.matches(
                        request.getOldPassword(),
                        currentUser.getPassword()
                );

        if (!matched) {

            throw new RuntimeException(
                    "Wrong old password"
            );
        }

        // =========================
        // UPDATE PASSWORD
        // =========================

        currentUser.setPassword(
                encoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(currentUser);
    }

    // =========================
    // UPDATE USER
    // =========================

    public void update(
            User currentUser,
            Long userId,
            UpdateUserRequest request
    ) {

        // =========================
        // PERMISSION
        // =========================

        PermissionUtil.requireManager(
                currentUser
        );

        // =========================
        // FIND USER
        // =========================

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        // =========================
        // HIERARCHY
        // =========================

        PermissionUtil.requireHigher(
                currentUser,
                user
        );

        // =========================
        // MANAGER LIMIT
        // =========================

        if (currentUser.getRole()
                == Role.MANAGER
                && request.getRole()
                != Role.EMPLOYEE) {

            throw new RuntimeException(
                    "Manager can only assign EMPLOYEE"
            );
        }

        // =========================
        // UPDATE
        // =========================

        user.setFullName(
                request.getFullName()
        );

        user.setPhone(
                request.getPhone()
        );

        user.setRole(
                request.getRole()
        );

        user.setActive(
                request.getActive()
        );

        userRepository.save(user);
    }
}