package com.ra.ptit_cntt2_it211_ss12_ex3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String customerName;
    private String product;
    private Integer quantity;
    private Double totalAmount;
}
