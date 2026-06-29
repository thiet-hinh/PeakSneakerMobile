package com.example.shoestore.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyVoucherResponse {
    private Boolean success;
    private String code;
    private BigDecimal discountAmount;
    private String message;
}
