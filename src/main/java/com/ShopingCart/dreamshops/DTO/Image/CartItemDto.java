package com.ShopingCart.dreamshops.DTO.Image;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long Id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
