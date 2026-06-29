package com.example.shoestore.repository;

import com.example.shoestore.dto.request.ProductFilterRequest;
import com.example.shoestore.dto.response.ProductCardDTO;
import com.example.shoestore.entity.Product;
import com.example.shoestore.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {


    List<Product> findByBrandIdAndIsDeletedFalse(Integer brandId);

    List<Product> findByIsFeaturedTrueAndIsDeletedFalse();

    List<Product> findByGenderAndIsDeletedFalse(Gender gender);


    //tìm bằng thanh tìm kiếm theo tên
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
           "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(@Param("keyword") String keyword);

    //tìm bằng bộ lọc
    @Query("""
    SELECT new com.example.shoestore.dto.response.ProductCardDTO(
        p.id, 
        p.name, 
        COALESCE(
            (SELECT img.imageName FROM Image img WHERE img.product.id = p.id AND img.isPrimary = true ORDER BY img.id ASC LIMIT 1),
            (SELECT img.imageName FROM Image img WHERE img.productVariant.product.id = p.id AND img.isPrimary = true ORDER BY img.id ASC LIMIT 1),
            (SELECT img.imageName FROM Image img WHERE img.product.id = p.id ORDER BY img.id ASC LIMIT 1)
        ), 
        b.name, 
        p.basePrice, 
        p.discountRate, 
        p.price,
        (SELECT AVG(r.star) FROM Review r WHERE r.product.id = p.id), 
        (SELECT COALESCE(SUM(oi.quantity), 0L) FROM OrderItem oi WHERE oi.productVariant.product.id = p.id)
    )
    FROM Product p 
    LEFT JOIN p.brand b 
    LEFT JOIN p.variants pv
    WHERE p.isDeleted = false 
      AND (:keyword IS NULL OR :keyword = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:gender IS NULL OR p.gender = :gender)
      AND (:brandId IS NULL OR b.id = :brandId)
      AND (:minPrice IS NULL OR p.price >= :minPrice)
      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
      AND (:size IS NULL OR :size = '' OR pv.size = :size)
    GROUP BY p.id, p.name, b.name, p.basePrice, p.discountRate, p.price
""")
    List<ProductCardDTO> findProductCards(
            @Param("keyword") String keyword, // Thêm param keyword vào đầu tiên
            @Param("gender") Gender gender,
            @Param("brandId") Integer brandId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("size") String size
    );
}
