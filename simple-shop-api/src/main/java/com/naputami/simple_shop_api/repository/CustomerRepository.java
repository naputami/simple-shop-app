package com.naputami.simple_shop_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.naputami.simple_shop_api.model.Customer;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    @Query("SELECT c from Customer c WHERE c.isActive = true")
    List<Customer> findActiveCustomers();
    
}