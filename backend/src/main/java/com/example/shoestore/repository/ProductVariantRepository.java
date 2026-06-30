package com.example.shoestore.repository;

import com.example.shoestore.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    List<ProductVariant> findByProductId(Integer productId);

    Optional<ProductVariant> findByProductIdAndColorAndSize(Integer productId, String color, String size);

    @Modifying
    @Query("UPDATE ProductVariant pv SET pv.stockQuantity = pv.stockQuantity - :quantity " +
           "WHERE pv.id = :id AND pv.stockQuantity >= :quantity")
    int decreaseStock(@Param("id") Integer id, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE ProductVariant pv SET pv.stockQuantity = pv.stockQuantity + :quantity WHERE pv.id = :id")
    int increaseStock(@Param("id") Integer id, @Param("quantity") Integer quantity);

}
