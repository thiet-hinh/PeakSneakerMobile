package com.example.shoestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCardDTO {

    private Integer id;
    private String productName;
    private String imageUrl;
    private String brandName;
    private BigDecimal basePrice;
    private BigDecimal discountRate;
    private BigDecimal price;
    private Double averageRating;
    private Long soldQuantity;

}
