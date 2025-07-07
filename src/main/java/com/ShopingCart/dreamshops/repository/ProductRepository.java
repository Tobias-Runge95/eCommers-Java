package com.ShopingCart.dreamshops.repository;

import com.ShopingCart.dreamshops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String categoryName);

    List<Product> findByBrandName(String brandName);

    List<Product> findByCategoryNameAndBrand(String categoryName, String brand);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brand, String brand1);

    Long countByPrandAndName(String name, String name1);
}
