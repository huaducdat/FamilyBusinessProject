package com.huaducdat.storemanager.customer.repository;

import com.huaducdat.storemanager.customer.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
    List<Customer> findByStoreId(
            Long storeId
    );

    Optional<Customer> findByIdAndStoreId(
            Long id,
            Long storeId
    );
}
