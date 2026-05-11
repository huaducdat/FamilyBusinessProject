package com.huaducdat.storemanager.service.supplier;

import com.huaducdat.storemanager.model.entity.Store;
import com.huaducdat.storemanager.model.entity.Supplier;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.CreateSupplierRequest;
import com.huaducdat.storemanager.model.response.SupplierResponse;
import com.huaducdat.storemanager.repository.SupplierRepository;
import com.huaducdat.storemanager.service.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(
            SupplierRepository supplierRepository
    ) {

        this.supplierRepository =
                supplierRepository;
    }

    // =========================
    // CREATE
    // =========================

    public Supplier create(
            User currentUser,
            CreateSupplierRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        Store store =
                currentUser.getStore();

        Supplier supplier =
                new Supplier();

        supplier.setStore(store);

        supplier.setName(
                request.getName()
        );

        supplier.setPhone(
                request.getPhone()
        );

        supplier.setAddress(
                request.getAddress()
        );

        supplier.setEmail(
                request.getEmail()
        );

        supplier.setActive(true);

        return supplierRepository
                .save(supplier);
    }

    // =========================
    // LIST
    // =========================

    public List<SupplierResponse> list() {

        return supplierRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // RESPONSE
    // =========================

    private SupplierResponse toResponse(
            Supplier supplier
    ) {

        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .phone(supplier.getPhone())
                .address(
                        supplier.getAddress()
                )
                .email(
                        supplier.getEmail()
                )
                .active(
                        supplier.getActive()
                )
                .build();
    }
}