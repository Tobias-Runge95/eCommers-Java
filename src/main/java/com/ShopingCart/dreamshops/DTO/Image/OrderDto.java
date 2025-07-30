package com.ShopingCart.dreamshops.DTO.Image;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long orderId;
    private Long userId;
    private LocalDateTime  orderDate;
    private BigDecimal orderPrice;
    private String orderStatus;
    private List<OrderItemDto> orderItems;
}
