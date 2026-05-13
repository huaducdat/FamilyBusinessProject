package com.huaducdat.storemanager.service.store;

import com.huaducdat.storemanager.model.entity.Store;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.UserRole;
import com.huaducdat.storemanager.model.request.CreateStoreRequest;
import com.huaducdat.storemanager.model.response.StoreResponse;
import com.huaducdat.storemanager.repository.StoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StoreService {

    private final StoreRepository repository;

    public StoreService(
            StoreRepository repository
    ) {

        this.repository =
                repository;
    }

    // =========================
    // CREATE STORE
    // =========================

    public Store create(
            User currentUser,
            CreateStoreRequest request
    ) {

        if (
                currentUser.getUserRole()
                        != UserRole.OWNER
        ) {

            throw new RuntimeException(
                    "Owner only"
            );
        }

        Store store =
                new Store();

        store.setName(
                request.getName()
        );

        store.setAddress(
                request.getAddress()
        );

        store.setPhone(
                request.getPhone()
        );

        return repository.save(store);
    }

    // =========================
    // LIST
    // =========================

    public List<StoreResponse> list() {

        return repository
                .findAll()
                .stream()
                .map(store ->

                        StoreResponse
                                .builder()

                                .id(
                                        store.getId()
                                )

                                .name(
                                        store.getName()
                                )

                                .address(
                                        store.getAddress()
                                )

                                .phone(
                                        store.getPhone()
                                )

                                .build()
                )
                .toList();
    }
}