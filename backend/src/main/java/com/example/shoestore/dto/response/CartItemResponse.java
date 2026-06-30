package com.example.shoestore.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CartItemResponse {
    private Integer cartItemId;
    private Integer productId;
    private Integer variantId;
    private String productName;
    private String imageUrl;
    private String color;
    private String size;
    private BigDecimal price;
    private Integer quantity;
    private Integer stockQuantity;
}