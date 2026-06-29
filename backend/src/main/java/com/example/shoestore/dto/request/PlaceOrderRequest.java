package com.example.shoestore.dto.request;

import com.example.shoestore.enums.DeliveryMethod;
import com.example.shoestore.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
    private Integer userId;
    private List<Integer> selectedCartItemIds;
    private String voucherCode;
    private PaymentMethod paymentMethod;
    private DeliveryMethod deliveryMethod;
}
