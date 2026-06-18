package com.example.shoestore.dto.request;

import com.example.shoestore.enums.Gender;

import java.math.BigDecimal;

public record ProductFilterRequest(Integer brandId,
                                   Gender gender,
                                   String size,
                                   String sort,
                                   BigDecimal minPrice,
                                   BigDecimal maxPrice) {
}
