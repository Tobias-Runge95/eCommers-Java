package com.ShopingCart.dreamshops.DTO.Image;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private BigDecimal productPrice;
    private Integer quantity;
}
