package com.example.shoestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutCartItem {
    private Integer cartItemId;
    private Integer variantId;
    private String imageUrl;
    private String brand;
    private String productName;
    private String color;
    private String size;
    private Integer quantity;
    private Double price;
}
