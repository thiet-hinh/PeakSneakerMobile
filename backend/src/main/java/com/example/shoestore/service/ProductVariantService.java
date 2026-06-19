package com.example.shoestore.service;

import com.example.shoestore.entity.ProductVariant;
import com.example.shoestore.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    public List<ProductVariant> findByProductId(Integer productId) {
        return productVariantRepository.findByProductId(productId);
    }

    public ProductVariant findById(Integer id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductVariant not found with id: " + id));
    }

    @Transactional
    public ProductVariant save(ProductVariant variant) {
        return productVariantRepository.save(variant);
    }

    @Transactional
    public ProductVariant update(Integer id, ProductVariant updated) {
        ProductVariant existing = findById(id);
        existing.setColor(updated.getColor());
        existing.setSize(updated.getSize());
        existing.setStockQuantity(updated.getStockQuantity());
        return productVariantRepository.save(existing);
    }

    @Transactional
    public void decreaseStock(Integer id, Integer quantity) {
        int rows = productVariantRepository.decreaseStock(id, quantity);
        if (rows == 0) {
            throw new RuntimeException("Insufficient stock for variant: " + id);
        }
    }

    @Transactional
    public void increaseStock(Integer id, Integer quantity) {
        productVariantRepository.increaseStock(id, quantity);
    }

}