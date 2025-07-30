package com.ShopingCart.dreamshops.service.Cart;

import com.ShopingCart.dreamshops.model.CartItem;
import com.ShopingCart.dreamshops.request.cartItem.AddCartItemRequest;
import com.ShopingCart.dreamshops.request.cartItem.UpdateCartItemRequest;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeCartItem(Long cartId, Long productId);
    void updateCartItem(UpdateCartItemRequest request);

    CartItem getCartItem(Long cartId, Long productId);
}
