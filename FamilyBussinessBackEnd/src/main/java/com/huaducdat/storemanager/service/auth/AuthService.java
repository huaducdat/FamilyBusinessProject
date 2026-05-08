package com.huaducdat.storemanager.service.auth;

import com.huaducdat.storemanager.model.entity.AuthToken;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.LoginRequest;
import com.huaducdat.storemanager.model.response.LoginResponse;
import com.huaducdat.storemanager.repository.AuthTokenRepository;
import com.huaducdat.storemanager.repository.UserRepository;
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

    public AuthService(
            UserRepository userRepository,
            BCryptPasswordEncoder encoder, AuthTokenRepository tokenRepository
    ) {

        this.userRepository = userRepository;

        this.encoder = encoder;
        this.tokenRepository = tokenRepository;
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

        return LoginResponse.builder().token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }



    public void logout(
            String token
    ) {

        tokenRepository.deleteByToken(
                token
        );
    }
}