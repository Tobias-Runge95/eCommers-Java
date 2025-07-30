package com.ShopingCart.dreamshops.repository;

import com.ShopingCart.dreamshops.model.Product;
import com.ShopingCart.dreamshops.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
