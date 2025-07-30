package com.ShopingCart.dreamshops.DTO.Image;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {
    private Long Id;
    private List<CartItemDto> cartItems;
    private BigDecimal totalAmount = BigDecimal.ZERO;

}
