package com.huaducdat.storemanager.service.user;

import com.huaducdat.storemanager.model.entity.Store;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.Role;
import com.huaducdat.storemanager.model.request.ChangePasswordRequest;
import com.huaducdat.storemanager.model.request.CreateUserRequest;
import com.huaducdat.storemanager.model.response.UserResponse;
import com.huaducdat.storemanager.repository.UserRepository;
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

    public User create(
            User currentUser,
            CreateUserRequest request
    ) {

        // =========================
        // ONLY ADMIN
        // =========================

        if (currentUser.getRole()
                != Role.ADMIN) {

            throw new RuntimeException(
                    "Permission denied"
            );
        }

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

    public List<UserResponse> list(
            User currentUser
    ) {

        // =========================
        // ONLY ADMIN
        // =========================

        if (currentUser.getRole()
                != Role.ADMIN) {

            throw new RuntimeException(
                    "Permission denied"
            );
        }

        return userRepository
                .findAll()
                .stream()
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

    public void toggleActive(
            User currentUser,
            Long userId,
            Boolean active
    ) {

        // =========================
        // ONLY ADMIN
        // =========================

        if (currentUser.getRole()
                != Role.ADMIN) {

            throw new RuntimeException(
                    "Permission denied"
            );
        }

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
}