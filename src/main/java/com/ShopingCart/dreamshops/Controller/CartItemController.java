package com.ShopingCart.dreamshops.Controller;

import com.ShopingCart.dreamshops.Response.ApiResponse;
import com.ShopingCart.dreamshops.exceptions.ResourceNotFoundException;
import com.ShopingCart.dreamshops.model.Cart;
import com.ShopingCart.dreamshops.model.User;
import com.ShopingCart.dreamshops.request.cartItem.AddCartItemRequest;
import com.ShopingCart.dreamshops.request.cartItem.UpdateCartItemRequest;
import com.ShopingCart.dreamshops.service.Cart.CartItemService;
import com.ShopingCart.dreamshops.service.Cart.ICartService;
import com.ShopingCart.dreamshops.service.User.IUserService;
import io.jsonwebtoken.JwtException;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItem")
public class CartItemController {
    private final CartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCartItem(@RequestBody AddCartItemRequest request){
        try{
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);
            cartItemService.addItemToCart(cart.getId(), request.productId, request.quantity);
            return ResponseEntity.ok().body(new ApiResponse("Added item", null));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Item not found", null));
        }catch(JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid token", null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> removeCartItem(@RequestParam Long cartId, @RequestParam Long productId){
        cartItemService.removeCartItem(cartId, productId);
        return ResponseEntity.ok().body(new ApiResponse("Deleted item", null));
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updateCartItem(@RequestBody UpdateCartItemRequest request){
        try{
            cartItemService.updateCartItem(request);
            return ResponseEntity.ok().body(new ApiResponse("Updated item", null));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Item not found", null));
        }
    }
}
