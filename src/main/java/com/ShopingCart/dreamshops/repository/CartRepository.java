package com.ShopingCart.dreamshops.repository;

import com.ShopingCart.dreamshops.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart getCartByUserId(Long userId);
}
