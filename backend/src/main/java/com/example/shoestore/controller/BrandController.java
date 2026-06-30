package com.example.shoestore.controller;

import com.example.shoestore.dto.response.BrandResponse;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAll() {
        return ResponseEntity.ok(brandService.findAllWithImage());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(brandService.findById(id));
    }

}
