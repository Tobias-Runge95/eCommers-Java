package com.ShopingCart.dreamshops.service.Cart;

import com.ShopingCart.dreamshops.model.Cart;
import com.ShopingCart.dreamshops.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotal(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
