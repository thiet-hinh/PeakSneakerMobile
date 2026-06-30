package com.example.shoestore.service;

import com.example.shoestore.dto.request.ProductFilterRequest;
import com.example.shoestore.dto.response.ProductCardDTO;
import com.example.shoestore.dto.response.ProductDetailDTO;
import com.example.shoestore.entity.Product;
import com.example.shoestore.entity.ProductVariant;
import com.example.shoestore.enums.Gender;
import com.example.shoestore.repository.ProductRepository;
import com.example.shoestore.repository.ProductVariantRepository;
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
    private final ProductVariantRepository productVariantRepository;

    public List<ProductCardDTO> getAllCardProduct(String keyword, Gender gender, Integer brandId, BigDecimal minPrice, BigDecimal maxPrice, String size){
        List<ProductCardDTO> cards = productRepository.findProductCards(keyword, gender, brandId, minPrice, maxPrice, size);
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

    @Transactional(readOnly = true)
    public ProductDetailDTO getDetailById(Integer id) {
        // 1. SỬA LẠI: Tìm đúng thực thể Product từ ProductRepository
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        // 2. Lấy tên Brand an toàn
        String brandName = (product.getBrand() != null) ? product.getBrand().getName() : "Shoe Shop";

        // 3. Lấy imageName và bọc qua Cloudinary Service
        String imageUrl = "";
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            String rawImageName = product.getImages().get(0).getImageName();
            if (rawImageName != null) {
                imageUrl = cloudinaryService.createImageUrl(rawImageName);
            }
        }

        // 4. Tính toán rating trung bình hoặc để mặc định
        String rating = "5.0";

        // 5. THÊM MỚI: Gọi Repository lấy toàn bộ danh sách biến thể (Màu, Size, Kho) của sản phẩm này lên
        List<ProductVariant> variants = productVariantRepository.findByProductId(id);

        // 6. Đóng gói gọn gàng chuyển về cho Android (Nhớ truyền biến variants vào cuối constructor)
        return new ProductDetailDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAttribute(),
                product.getBasePrice(),
                product.getPrice(),
                product.getDiscountRate(),
                brandName,
                imageUrl,
                rating,
                variants // 👈 ĐÂY CHÍNH LÀ CHÌA KHÓA để Android nhận được danh sách màu và kích cỡ!
        );
    }

    @Transactional
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }
}