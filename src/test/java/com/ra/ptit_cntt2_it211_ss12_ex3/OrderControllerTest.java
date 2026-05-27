package com.ra.ptit_cntt2_it211_ss12_ex3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.ptit_cntt2_it211_ss12_ex3.controller.OrderController;
import com.ra.ptit_cntt2_it211_ss12_ex3.entity.Order;
import com.ra.ptit_cntt2_it211_ss12_ex3.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrder = new Order(1L, "Nguyen Van A", "Laptop", 1, 1500.0);
    }

    @Test
    void getAllOrders_ReturnsHttp200AndJsonArray() throws Exception {
        List<Order> orders = Arrays.asList(sampleOrder);
        Mockito.when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(orders.size()))
                .andExpect(jsonPath("$[0].id").value(sampleOrder.getId()))
                .andExpect(jsonPath("$[0].customerName").value(sampleOrder.getCustomerName()));
    }

    @Test
    void getOrderById_Found_ReturnsHttp200() throws Exception {
        Mockito.when(orderService.getOrderById(1L)).thenReturn(sampleOrder);

        mockMvc.perform(get("/api/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleOrder.getId()))
                .andExpect(jsonPath("$.customerName").value(sampleOrder.getCustomerName()));
    }

    @Test
    void getOrderById_NotFound_ReturnsHttp404() throws Exception {
        Mockito.when(orderService.getOrderById(999L)).thenThrow(new RuntimeException("Order not found with id: 999"));

        mockMvc.perform(get("/api/orders/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addOrder_ReturnsHttp201AndBodyContainsId() throws Exception {
        Order newOrder = new Order(null, "Tran Thi B", "Mouse", 2, 50.0);
        Order savedOrder = new Order(2L, "Tran Thi B", "Mouse", 2, 50.0);
        
        Mockito.when(orderService.addOrder(any(Order.class))).thenReturn(savedOrder);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.customerName").value(savedOrder.getCustomerName()));
    }
}
