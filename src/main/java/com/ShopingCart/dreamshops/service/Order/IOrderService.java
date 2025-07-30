package com.ShopingCart.dreamshops.service.Order;

import com.ShopingCart.dreamshops.DTO.Image.OrderDto;
import com.ShopingCart.dreamshops.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
