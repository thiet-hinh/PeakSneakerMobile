package com.example.shoestore.controller;

import com.example.shoestore.dto.request.ProductFilterRequest;
import com.example.shoestore.dto.response.ProductCardDTO;
import com.example.shoestore.dto.response.ProductDetailDTO;
import com.example.shoestore.entity.Product;
import com.example.shoestore.enums.Gender;
import com.example.shoestore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductCardDTO>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(required = false) String size) {

        // Gửi toàn bộ dữ liệu lọc sang Service
        List<ProductCardDTO> products = productService.getAllCardProduct(keyword, gender, brandId, minPrice, maxPrice, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getDetailById(id));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Product>> getFeatured() {
        return ResponseEntity.ok(productService.findFeatured());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(
            @RequestParam String keyword) {
        return ResponseEntity.ok(productService.search(keyword));
    }


    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Integer id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Integer id) {
        productService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}