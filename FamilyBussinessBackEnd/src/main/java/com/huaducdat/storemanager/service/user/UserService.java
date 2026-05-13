package com.huaducdat.storemanager.service.user;

import com.huaducdat.storemanager.model.entity.Store;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.AuditAction;
import com.huaducdat.storemanager.model.enumtype.UserRole;
import com.huaducdat.storemanager.model.request.ChangePasswordRequest;
import com.huaducdat.storemanager.model.request.CreateUserRequest;
import com.huaducdat.storemanager.model.request.UpdateUserRequest;
import com.huaducdat.storemanager.model.response.UserResponse;
import com.huaducdat.storemanager.repository.StoreRepository;
import com.huaducdat.storemanager.repository.UserRepository;
import com.huaducdat.storemanager.service.audit.AuditService;
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

    private final AuditService auditService;

    private final StoreRepository storeRepository;

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder encoder, AuditService auditService, StoreRepository storeRepository
    ) {

        this.userRepository = userRepository;

        this.encoder = encoder;
        this.auditService = auditService;
        this.storeRepository = storeRepository;
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

        if (currentUser.getUserRole()
                == UserRole.MANAGER
                && request.getUserRole()
                != UserRole.EMPLOYEE) {

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

        user.setUserRole(
                request.getUserRole()
        );

        user.setActive(true);

        User savedUser =
                userRepository.save(user);

        auditService.log(
                currentUser,
                AuditAction.CREATE_USER,
                "Created user: "
                        + savedUser.getUsername()
        );

        return savedUser;
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

                    if (currentUser.getUserRole()
                            == UserRole.ADMIN) {

                        return true;
                    }

                    return user.getUserRole()
                            == UserRole.EMPLOYEE;
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
                                .userRole(
                                        user.getUserRole()
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

        if (currentUser.getUserRole()
                == UserRole.MANAGER
                && request.getUserRole()
                != UserRole.EMPLOYEE) {

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

        user.setUserRole(
                request.getUserRole()
        );

        user.setActive(
                request.getActive()
        );

        userRepository.save(user);
    }

    private UserResponse toResponse(
            User user
    ) {

        return UserResponse.builder()

                .id(
                        user.getId()
                )

                .username(
                        user.getUsername()
                )

                .fullName(
                        user.getFullName()
                )

                .userRole(
                        user.getUserRole()
                )

                .active(
                        user.getActive()
                )

                .storeId(
                        user.getStore()
                                .getId()
                )

                .storeName(
                        user.getStore()
                                .getName()
                )

                .build();
    }

    public void transferStore(
            User currentUser,
            Long userId,
            Long storeId
    ) {


        System.out.println(
                currentUser.getUsername()
        );

        System.out.println(
                currentUser.getUserRole()
        );


        PermissionUtil.requireManager(
                currentUser
        );

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        Store store =
                storeRepository
                        .findById(storeId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Store not found"
                                )
                        );

        user.setStore(store);

        userRepository.save(user);

        auditService.log(
                currentUser,
                AuditAction.UPDATE_USER,
                "Transferred user "
                        + user.getUsername()
                        + " to store "
                        + store.getName()
        );
    }
}