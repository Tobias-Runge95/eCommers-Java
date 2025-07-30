package com.ShopingCart.dreamshops.repository;

import com.ShopingCart.dreamshops.model.Cart;
import com.ShopingCart.dreamshops.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
