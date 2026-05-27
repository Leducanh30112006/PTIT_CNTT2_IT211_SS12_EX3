package com.ra.ptit_cntt2_it211_ss12_ex3;

import com.ra.ptit_cntt2_it211_ss12_ex3.entity.Order;
import com.ra.ptit_cntt2_it211_ss12_ex3.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        // Khởi tạo một đối tượng Order mẫu để dùng cho các bài test
        sampleOrder = new Order(null, "Nguyen Van A", "Laptop", 1, 1500.0);
    }

    @Test
    void getAllOrders_ReturnNonEmptyList() {
        orderService.addOrder(sampleOrder);
        
        List<Order> orders = orderService.getAllOrders();
        
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
    }

    @Test
    void getOrderById_Found() {
        Order addedOrder = orderService.addOrder(sampleOrder);
        
        Order foundOrder = orderService.getOrderById(addedOrder.getId());
        
        assertNotNull(foundOrder);
        assertEquals(addedOrder.getId(), foundOrder.getId());
        assertEquals(addedOrder.getCustomerName(), foundOrder.getCustomerName());
    }

    @Test
    void getOrderById_NotFound_ThrowException() {
        Long nonExistentId = 999L;
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.getOrderById(nonExistentId);
        });
        
        assertEquals("Order not found with id: " + nonExistentId, exception.getMessage());
    }

    @Test
    void addOrder_Success() {
        Order newOrder = new Order(null, "Tran Thi B", "Mouse", 2, 50.0);
        
        Order savedOrder = orderService.addOrder(newOrder);
        
        assertNotNull(savedOrder.getId());
        assertEquals("Tran Thi B", savedOrder.getCustomerName());
        assertEquals("Mouse", savedOrder.getProduct());
        assertEquals(2, savedOrder.getQuantity());
        assertEquals(50.0, savedOrder.getTotalAmount());
    }

    @Test
    void updateOrder_Success() {
        Order addedOrder = orderService.addOrder(sampleOrder);
        Order updatedInfo = new Order(null, "Nguyen Van A (Updated)", "Laptop Pro", 2, 3000.0);
        
        Order result = orderService.updateOrder(addedOrder.getId(), updatedInfo);
        
        assertNotNull(result);
        assertEquals(addedOrder.getId(), result.getId());
        assertEquals("Nguyen Van A (Updated)", result.getCustomerName());
        assertEquals("Laptop Pro", result.getProduct());
        assertEquals(2, result.getQuantity());
        assertEquals(3000.0, result.getTotalAmount());
    }

    @Test
    void deleteOrder_RemovesElement() {
        Order addedOrder = orderService.addOrder(sampleOrder);
        int initialSize = orderService.getAllOrders().size();
        
        orderService.deleteOrder(addedOrder.getId());
        
        int finalSize = orderService.getAllOrders().size();
        assertEquals(initialSize - 1, finalSize);
        assertThrows(RuntimeException.class, () -> orderService.getOrderById(addedOrder.getId()));
    }
}
