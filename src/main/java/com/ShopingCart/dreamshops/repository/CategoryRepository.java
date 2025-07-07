package com.ShopingCart.dreamshops.repository;

import com.ShopingCart.dreamshops.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
