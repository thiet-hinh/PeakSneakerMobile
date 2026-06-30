package com.example.shoestore.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {
    private Integer userId;
    private Integer productId;
    private String color;
    private String size;
    private Integer quantity;
}