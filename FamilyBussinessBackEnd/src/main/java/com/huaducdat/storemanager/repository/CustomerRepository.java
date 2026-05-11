package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {
}