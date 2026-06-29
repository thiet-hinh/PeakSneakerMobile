package com.example.shoestore.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderResponse {
    private String orderCode;
    private LocalDate estimatedDeliveryDate;
    private String paymentMethod;
    private String message;

}
