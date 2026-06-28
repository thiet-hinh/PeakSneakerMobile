package com.example.shoestore.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Integer id;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private BigDecimal finalAmount;
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderItemDetailResponse> items;
}
