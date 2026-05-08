package com.huaducdat.storemanager.config;

import com.huaducdat.storemanager.model.entity.Store;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.Role;
import com.huaducdat.storemanager.repository.StoreRepository;
import com.huaducdat.storemanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartupDataLoader
        implements CommandLineRunner {

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    public StartupDataLoader(
            StoreRepository storeRepository,
            UserRepository userRepository,
            BCryptPasswordEncoder encoder
    ) {

        this.storeRepository = storeRepository;

        this.userRepository = userRepository;

        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        try {

            // =========================
            // ALREADY INIT
            // =========================

            if (storeRepository.count() > 0) {

                System.out.println(
                        "⚡ DATA ALREADY EXISTS"
                );

                return;
            }

            // =========================
            // CREATE STORE
            // =========================

            Store store = new Store();

            store.setName("Default Store");

            store.setAddress("Nam Dinh");

            store.setPhone("0123456789");

            storeRepository.save(store);

            // =========================
            // CREATE ADMIN
            // =========================

            User admin = new User();

            admin.setStore(store);

            admin.setUsername("admin");

            admin.setPassword(
                    encoder.encode("123456")
            );

            admin.setFullName(
                    "System Admin"
            );

            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println(
                    "🔥 DEFAULT ADMIN CREATED"
            );

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}