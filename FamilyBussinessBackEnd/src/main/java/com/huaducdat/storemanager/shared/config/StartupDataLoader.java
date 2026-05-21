package com.huaducdat.storemanager.shared.config;

import com.huaducdat.storemanager.auth.model.entity.Owner;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.UserRole;
import com.huaducdat.storemanager.auth.repository.OwnerRepository;
import com.huaducdat.storemanager.store.repository.StoreRepository;
import com.huaducdat.storemanager.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupDataLoader
        implements CommandLineRunner {

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;

    private final OwnerRepository ownerRepository;

    private final BCryptPasswordEncoder encoder;

    public StartupDataLoader(
            StoreRepository storeRepository,
            UserRepository userRepository,
            OwnerRepository ownerRepository,
            BCryptPasswordEncoder encoder
    ) {

        this.storeRepository = storeRepository;

        this.userRepository = userRepository;

        this.ownerRepository = ownerRepository;

        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        try {

            Owner owner =
                    ownerRepository
                            .findByUsername("owner")
                            .orElseGet(() -> {
                                Owner created = new Owner();
                                created.setUsername("owner");
                                created.setPassword(
                                        encoder.encode("123456")
                                );
                                created.setFullName("Default Owner");
                                created.setPhone("0123456789");
                                created.setActive(true);
                                return ownerRepository.save(created);
                            });

            List<Store> stores = storeRepository.findAll();

            if (stores.isEmpty()) {
                Store store = new Store();
                store.setOwner(owner);
                store.setName("Default Store");
                store.setAddress("Nam Dinh");
                store.setPhone("0123456789");
                storeRepository.save(store);
                stores = storeRepository.findAll();
            } else {
                for (Store store : stores) {
                    if (store.getOwner() == null) {
                        store.setOwner(owner);
                        storeRepository.save(store);
                    }
                }
            }

            Store defaultStore =
                    stores.get(0);

            // =========================
            // CREATE ADMIN
            // =========================

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();

                admin.setStore(defaultStore);

                admin.setUsername("admin");

                admin.setPassword(
                        encoder.encode("123456")
                );

                admin.setFullName(
                        "System Admin"
                );

                admin.setUserRole(UserRole.ADMIN);
                admin.setActive(true);

                userRepository.save(admin);
            }

            System.out.println(
                    "🔥 DEFAULT OWNER DATA READY"
            );

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}
