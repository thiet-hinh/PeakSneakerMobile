package com.example.shoestore.controller;

import com.example.shoestore.entity.ProductVariant;
import com.example.shoestore.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/{productId}/variant")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @GetMapping
    public ResponseEntity<List<ProductVariant>> getByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(productVariantService.findByProductId(productId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productVariantService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductVariant> create(@RequestBody ProductVariant variant) {
        return ResponseEntity.ok(productVariantService.save(variant));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ProductVariant> update(
            @PathVariable Integer id,
            @RequestBody ProductVariant variant) {
        return ResponseEntity.ok(productVariantService.update(id, variant));
    }

    @GetMapping("/{id}/stock/decrease")
    public ResponseEntity<Void> decreaseStock(
            @PathVariable Integer id,
            @RequestParam Integer quantity) {
        productVariantService.decreaseStock(id, quantity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stock/increase")
    public ResponseEntity<Void> increaseStock(
            @PathVariable Integer id,
            @RequestParam Integer quantity) {
        productVariantService.increaseStock(id, quantity);
        return ResponseEntity.noContent().build();
    }

}
