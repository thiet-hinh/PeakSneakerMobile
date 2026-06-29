package com.example.shoestore.dto.response;

import java.math.BigDecimal;

public record ProductDetailDTO(
        Integer id,
        String name,
        String description,
        String attribute,
        BigDecimal basePrice,
        BigDecimal price,
        BigDecimal discountRate,
        String brandName,
        String imageUrl,
        String rating
) {}