package com.example.shoestore.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyVoucherRequest {
    private Integer userId;
    private BigDecimal orderAmount;
    private String voucherCode;
}
