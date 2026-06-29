package com.example.shoestore.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDetailResponse {
    private String productName;
    private String brand;
    private String color;
    private String size;
    private Integer quantity;
    private BigDecimal price;
    private String imageUrl;
}
