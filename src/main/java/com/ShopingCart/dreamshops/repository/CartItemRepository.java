package com.ShopingCart.dreamshops.repository;

import com.ShopingCart.dreamshops.model.Cart;
import com.ShopingCart.dreamshops.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);
}
