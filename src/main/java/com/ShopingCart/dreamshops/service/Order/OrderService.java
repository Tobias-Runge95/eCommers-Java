package com.ShopingCart.dreamshops.service.Order;


import com.ShopingCart.dreamshops.DTO.Image.OrderDto;
import com.ShopingCart.dreamshops.enums.OrderStatus;
import com.ShopingCart.dreamshops.model.Cart;
import com.ShopingCart.dreamshops.model.Order;
import com.ShopingCart.dreamshops.model.OrderItem;
import com.ShopingCart.dreamshops.model.Product;
import com.ShopingCart.dreamshops.repository.OrderRepository;
import com.ShopingCart.dreamshops.repository.ProductRepository;
import com.ShopingCart.dreamshops.service.Cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper mapper;

    @Override
    public Order placeOrder(Long userId){
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(orderItems);
        order.setTotalAmount(calculateTotalAmount(orderItems));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
        // mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(this :: convertToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Not Found"));
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(product, order, cartItem.getQuantity(), cartItem.getUnitPrice());
        }).toList();
    }

    private Order createOrder(Cart cart){
        Order newOrder = new Order();
        newOrder.setUser(cart.getUser());
        newOrder.setOrderStatus(OrderStatus.PENDING);
        newOrder.setOrderDate(LocalDateTime.now());
        return newOrder;
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this :: convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order){
        return mapper.map(order, OrderDto.class);
    }
}
