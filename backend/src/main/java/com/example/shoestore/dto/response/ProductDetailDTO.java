package com.example.shoestore.dto.response;

import com.example.shoestore.entity.ProductVariant;

import java.math.BigDecimal;
import java.util.List;

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
        String rating,
        List<ProductVariant> variants
) {}