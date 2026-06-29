package com.example.shoestore.controller;

import com.example.shoestore.dto.request.ApplyVoucherRequest;
import com.example.shoestore.dto.response.ApplyVoucherResponse;
import com.example.shoestore.entity.Voucher;
import com.example.shoestore.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voucher")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping("/{code}")
    public ResponseEntity<Voucher> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(voucherService.findByCode(code));
    }

    @PostMapping("/apply")
    public ResponseEntity<ApplyVoucherResponse> applyVoucher(
            @RequestBody ApplyVoucherRequest request) {

        return ResponseEntity.ok(
                voucherService.applyVoucher(request)
        );
    }

}
