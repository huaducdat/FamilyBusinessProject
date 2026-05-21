package com.huaducdat.storemanager.store.service;

import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.UserRole;
import com.huaducdat.storemanager.store.dto.request.CreateStoreRequest;
import com.huaducdat.storemanager.store.dto.response.StoreResponse;
import com.huaducdat.storemanager.store.repository.StoreRepository;
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

        if (currentUser.getStore() == null
                || currentUser.getStore().getOwner() == null) {
            throw new RuntimeException(
                    "Owner context not found"
            );
        }

        store.setOwner(
                currentUser.getStore()
                        .getOwner()
        );

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

    public List<StoreResponse> list(
            User currentUser
    ) {

        if (currentUser.getStore() == null
                || currentUser.getStore().getOwner() == null) {
            throw new RuntimeException(
                    "Owner context not found"
            );
        }

        return repository
                .findByOwnerId(
                        currentUser.getStore()
                                .getOwner()
                                .getId()
                )
                .stream()
                .map(store ->

                        StoreResponse
                                .builder()

                                .id(
                                        store.getId()
                                )

                                .ownerId(
                                        store.getOwner() != null
                                                ? store.getOwner()
                                                .getId()
                                                : null
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
