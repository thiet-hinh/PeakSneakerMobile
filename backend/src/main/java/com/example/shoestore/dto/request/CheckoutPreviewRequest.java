package com.example.shoestore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutPreviewRequest {
    private List<Integer> cartItemIds;
}