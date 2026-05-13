package com.huaducdat.storemanager.service.auth;

import com.huaducdat.storemanager.model.entity.AuthToken;
import com.huaducdat.storemanager.model.entity.Store;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.AuditAction;
import com.huaducdat.storemanager.model.enumtype.UserRole;
import com.huaducdat.storemanager.model.request.CreateOwnerRequest;
import com.huaducdat.storemanager.model.request.LoginRequest;
import com.huaducdat.storemanager.model.response.LoginResponse;
import com.huaducdat.storemanager.repository.AuthTokenRepository;
import com.huaducdat.storemanager.repository.StoreRepository;
import com.huaducdat.storemanager.repository.UserRepository;
import com.huaducdat.storemanager.service.audit.AuditService;
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

    public AuthService(
            UserRepository userRepository,
            BCryptPasswordEncoder encoder, AuthTokenRepository tokenRepository, AuditService auditService, StoreRepository storeRepository
    ) {

        this.userRepository = userRepository;

        this.encoder = encoder;
        this.tokenRepository = tokenRepository;
        this.auditService = auditService;
        this.storeRepository = storeRepository;
    }

    public LoginResponse login(
            LoginRequest request
    ) {

        User user = userRepository
                .findByUsername(
                        request.getUsername()
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        if (!user.getActive()) {

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
                .build();
    }



    public void logout(
            String token
    ) {

        tokenRepository.deleteByToken(
                token
        );
    }

    public void createOwner(
            CreateOwnerRequest request
    ) {

        boolean ownerExists =
                userRepository
                        .existsByUserRole(
                                UserRole.OWNER
                        );

        if (ownerExists) {

            throw new RuntimeException(
                    "Owner already exists"
            );
        }

        Store store =
                new Store();

        store.setName(
                "Main Store"
        );

        store.setAddress(
                "Default Address"
        );

        store.setPhone(
                request.getPhone()
        );

        storeRepository.save(store);

        User owner =
                new User();

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

        owner.setUserRole(
                UserRole.OWNER
        );

        owner.setActive(true);

        owner.setStore(store);

        userRepository.save(owner);
    }
}