package com.ShopingCart.dreamshops.service.Cart;

import com.ShopingCart.dreamshops.exceptions.ResourceNotFoundException;
import com.ShopingCart.dreamshops.model.Cart;
import com.ShopingCart.dreamshops.model.CartItem;
import com.ShopingCart.dreamshops.model.Product;
import com.ShopingCart.dreamshops.repository.CartItemRepository;
import com.ShopingCart.dreamshops.repository.CartRepository;
import com.ShopingCart.dreamshops.request.cartItem.AddCartItemRequest;
import com.ShopingCart.dreamshops.request.cartItem.UpdateCartItemRequest;
import com.ShopingCart.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartITem = new CartItem();
        if(cart.getCartItems() == null){
            cartITem.setCart(cart);
            cartITem.setProduct(product);
            cartITem.setQuantity(quantity);
            cartITem.setUnitPrice(product.getPrice());
            cart.addItem(cartITem);
        }
        else{
            cartITem = cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getProduct()
                            .getId().equals(productId)).findFirst().orElse(new CartItem());
            if(cartITem.getId() == null) {
                cartITem.setCart(cart);
                cartITem.setProduct(product);
                cartITem.setQuantity(quantity);
                cartITem.setUnitPrice(product.getPrice());
                cart.addItem(cartITem);
            }
            else{
                cartITem.setQuantity(cartITem.getQuantity() + quantity);
            }
        }

        cartITem.setTotalPrice();
        cart.updateTotalAmount();
        cartItemRepository.save(cartITem);
        cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItem(UpdateCartItemRequest request) {
        Cart cart = cartService.getCart(request.cartId);
        cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(request.quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        //BigDecimal totalAmount = cart.getTotalAmount();
        //cart.setTotalAmount(totalAmount);
        cart.updateTotalAmount();
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}
