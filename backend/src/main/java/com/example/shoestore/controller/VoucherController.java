package com.example.shoestore.controller;

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


    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(voucherService.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Voucher> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(voucherService.findByCode(code));
    }

    @PostMapping
    public ResponseEntity<Voucher> create(@RequestBody Voucher voucher) {
        return ResponseEntity.ok(voucherService.save(voucher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voucher> update(@PathVariable Integer id, @RequestBody Voucher voucher) {
        return ResponseEntity.ok(voucherService.update(id, voucher));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> setActive(@PathVariable Integer id, @RequestParam Boolean isActive) {
        voucherService.setActive(id, isActive);
        return ResponseEntity.noContent().build();
    }

}
