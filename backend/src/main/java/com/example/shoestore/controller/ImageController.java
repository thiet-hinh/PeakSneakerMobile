package com.example.shoestore.controller;

import com.example.shoestore.entity.Image;
import com.example.shoestore.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/product/{productId}/primary")
    public ResponseEntity<Image> getPrimaryByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(imageService.findPrimaryByProductId(productId));
    }

    @GetMapping("/variant/{variantId}")
    public ResponseEntity<List<Image>> getByVariant(@PathVariable Integer variantId) {
        return ResponseEntity.ok(imageService.findByProductVariantId(variantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(imageService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Image> create(@RequestBody Image image) {
        return ResponseEntity.ok(imageService.save(image));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Image>> createBatch(@RequestBody List<Image> images) {
        return ResponseEntity.ok(imageService.saveAll(images));
    }

    @PatchMapping("/{id}/primary")
    public ResponseEntity<Void> setPrimary(@PathVariable Integer id, @RequestParam Integer productId) {
        imageService.setPrimary(id, productId);
        return ResponseEntity.noContent().build();
    }
}