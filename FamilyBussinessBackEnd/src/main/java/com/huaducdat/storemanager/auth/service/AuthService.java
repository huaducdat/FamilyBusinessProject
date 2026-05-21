package com.huaducdat.storemanager.auth.service;

import com.huaducdat.storemanager.auth.model.entity.AuthToken;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.auth.model.entity.Owner;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.AuditAction;
import com.huaducdat.storemanager.model.enumtype.UserRole;
import com.huaducdat.storemanager.auth.dto.request.CreateOwnerRequest;
import com.huaducdat.storemanager.auth.dto.request.LoginRequest;
import com.huaducdat.storemanager.auth.dto.response.LoginResponse;
import com.huaducdat.storemanager.auth.repository.AuthTokenRepository;
import com.huaducdat.storemanager.auth.repository.OwnerRepository;
import com.huaducdat.storemanager.store.repository.StoreRepository;
import com.huaducdat.storemanager.user.repository.UserRepository;
import com.huaducdat.storemanager.audit.service.AuditService;
import com.huaducdat.storemanager.shared.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Transactional
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final AuthTokenRepository tokenRepository;

    private final AuditService auditService;

    private final StoreRepository storeRepository;

    private final OwnerRepository ownerRepository;

    public AuthService(
            UserRepository userRepository,
            BCryptPasswordEncoder encoder,
            AuthTokenRepository tokenRepository,
            AuditService auditService,
            StoreRepository storeRepository,
            OwnerRepository ownerRepository
    ) {

        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokenRepository = tokenRepository;
        this.auditService = auditService;
        this.storeRepository = storeRepository;
        this.ownerRepository = ownerRepository;
    }

    public LoginResponse login(
            LoginRequest request
    ) {

        if (request == null
                || request.getUsername() == null
                || request.getUsername().isBlank()
                || request.getPassword() == null
                || request.getPassword().isBlank()) {

            throw new RuntimeException(
                    "Username and password are required"
            );
        }

        User user = userRepository
                .findByUsername(
                        request.getUsername()
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        if (!Boolean.TRUE.equals(user.getActive())) {

            throw new RuntimeException(
                    "User disabled"
            );
        }

        boolean matched =
                encoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!matched) {

            throw new RuntimeException(
                    "Wrong password"
            );
        }

        String token =
                UUID.randomUUID().toString();

        AuthToken authToken =
                new AuthToken();

        authToken.setUser(user);
        authToken.setStore(
                user.getStore()
        );

        authToken.setToken(token);

        authToken.setExpiredAt(
                LocalDateTime.now().plusDays(7)
        );

        tokenRepository.save(authToken);

        auditService.log(
                user,
                AuditAction.LOGIN,
                "User login"
        );

        return LoginResponse.builder().token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .userRole(user.getUserRole())
                .ownerId(
                        user.getStore() != null
                                && user.getStore().getOwner() != null
                                ? user.getStore().getOwner().getId()
                                : null
                )
                .storeId(
                        user.getStore() != null
                                ? user.getStore().getId()
                                : null
                )
                .storeName(
                        user.getStore() != null
                                ? user.getStore().getName()
                                : null
                )
                .build();
    }



    public void logout(
            String token
    ) {

        if (token == null || token.isBlank()) {

            throw new UnauthorizedException(
                    "Missing token"
            );
        }

        tokenRepository.deleteByToken(
                token
        );
    }

    public void createOwner(
            CreateOwnerRequest request
    ) {

        if (request == null
                || request.getUsername() == null
                || request.getUsername().isBlank()
                || request.getPassword() == null
                || request.getPassword().isBlank()
                || request.getFullName() == null
                || request.getFullName().isBlank()) {

            throw new RuntimeException(
                    "Username, password and full name are required"
            );
        }

        boolean usernameExists =
                userRepository
                        .findByUsername(
                                request.getUsername()
                        )
                        .isPresent()
                        || ownerRepository
                        .existsByUsername(
                                request.getUsername()
                        );

        if (usernameExists) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        Owner owner =
                new Owner();

        owner.setUsername(
                request.getUsername()
        );

        owner.setPassword(
                encoder.encode(
                        request.getPassword()
                )
        );

        owner.setFullName(
                request.getFullName()
        );

        owner.setPhone(
                request.getPhone()
        );

        owner.setActive(true);

        owner =
                ownerRepository.save(
                        owner
                );

        Store store =
                new Store();

        store.setOwner(owner);

        store.setName(
                "Main Store"
        );

        store.setAddress(
                "Default Address"
        );

        store.setPhone(
                request.getPhone()
        );

        store =
                storeRepository.save(store);

        User ownerUser =
                new User();

        ownerUser.setStore(
                store
        );

        ownerUser.setUsername(
                request.getUsername()
        );

        ownerUser.setPassword(
                encoder.encode(
                        request.getPassword()
                )
        );

        ownerUser.setFullName(
                request.getFullName()
        );

        ownerUser.setPhone(
                request.getPhone()
        );

        ownerUser.setUserRole(
                UserRole.OWNER
        );

        ownerUser.setActive(true);

        userRepository.save(ownerUser);
    }
}
