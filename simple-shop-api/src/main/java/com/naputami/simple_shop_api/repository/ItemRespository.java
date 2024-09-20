package com.naputami.simple_shop_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.naputami.simple_shop_api.model.Item;
import java.util.List;

public interface ItemRespository extends JpaRepository<Item, String>, JpaSpecificationExecutor<Item>{
    @Query("SELECT i from Item i WHERE i.isAvailable = true")
    List<Item> findAvailableItems();
} 