package com.ra.ptit_cntt2_it211_ss12_ex3.service;

import com.ra.ptit_cntt2_it211_ss12_ex3.entity.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {
    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Order> getAllOrders() {
        return orders;
    }

    public Order getOrderById(Long id) {
        return orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public Order addOrder(Order order) {
        order.setId(idGenerator.getAndIncrement());
        orders.add(order);
        return order;
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        Order order = getOrderById(id);
        order.setCustomerName(updatedOrder.getCustomerName());
        order.setProduct(updatedOrder.getProduct());
        order.setQuantity(updatedOrder.getQuantity());
        order.setTotalAmount(updatedOrder.getTotalAmount());
        return order;
    }

    public boolean deleteOrder(Long id) {
        Order order = getOrderById(id);
        return orders.remove(order);
    }
}