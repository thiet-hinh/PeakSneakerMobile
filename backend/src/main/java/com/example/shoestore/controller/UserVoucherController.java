package com.example.shoestore.controller;

import com.example.shoestore.entity.UserVoucher;
import com.example.shoestore.service.UserVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserVoucherController {

    private final UserVoucherService userVoucherService;

    @GetMapping("/user-vouchers/{id}")
    public ResponseEntity<UserVoucher> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(userVoucherService.findById(id));
    }

    @PutMapping("/user-vouchers/{id}/use")
    public ResponseEntity<Void> setUsed(@PathVariable Integer id) {
        userVoucherService.markAsUnused(id);
        return ResponseEntity.noContent().build();
    }

}
