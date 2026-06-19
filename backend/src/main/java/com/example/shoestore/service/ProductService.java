package com.example.shoestore.service;

import com.example.shoestore.dto.request.ProductFilterRequest;
import com.example.shoestore.dto.response.ProductCardDTO;
import com.example.shoestore.entity.Product;
import com.example.shoestore.enums.Gender;
import com.example.shoestore.repository.ProductRepository;
import com.example.shoestore.thirdparty.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    public List<ProductCardDTO> getAllCardProduct(Gender gender,Integer brandId, BigDecimal minPrice,BigDecimal maxPrice,String size){
       List<ProductCardDTO> cards= productRepository.findProductCards( gender, brandId,  minPrice, maxPrice, size);
        cards.forEach(card -> {
            if (card.getImageUrl() != null) {
                card.setImageUrl(cloudinaryService.createImageUrl(card.getImageUrl()));
            }
        });
        return cards;
    }


    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public List<Product> findByBrand(Integer brandId) {
        return productRepository.findByBrandIdAndIsDeletedFalse(brandId);
    }

    public List<Product> findByGender(Gender gender) {
        return productRepository.findByGenderAndIsDeletedFalse(gender);
    }

    public List<Product> findFeatured() {
        return productRepository.findByIsFeaturedTrueAndIsDeletedFalse();
    }

    public List<Product> search(String keyword) {
        return productRepository.searchByName(keyword);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product update(Integer id, Product updated) {
        Product existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setAttribute(updated.getAttribute());
        existing.setBasePrice(updated.getBasePrice());
        existing.setDiscountRate(updated.getDiscountRate());
        existing.setGender(updated.getGender());
        existing.setProductType(updated.getProductType());
        existing.setIsFeatured(updated.getIsFeatured());
        existing.setBrand(updated.getBrand());
        existing.setCategory(updated.getCategory());
        return productRepository.save(existing);
    }

    @Transactional
    public void softDelete(Integer id) {
        Product product = findById(id);
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Transactional
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }
}