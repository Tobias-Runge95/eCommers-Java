package com.ShopingCart.dreamshops.repository;

import com.ShopingCart.dreamshops.model.Cart;
import com.ShopingCart.dreamshops.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
