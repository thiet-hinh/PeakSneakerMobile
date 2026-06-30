package com.example.shoestore.repository;

import com.example.shoestore.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    Optional<Image> findFirstByProductVariantIdAndIsPrimaryTrue(Integer variantId);

    List<Image> findByProductVariantId(Integer productVariantId);

    Optional<Image> findByProductIdAndIsPrimaryTrue(Integer productId);

    Optional<Image> findByImageName(String imageName);

    Optional<Image> findFirstByProductIdAndIsPrimaryTrue(Integer productId);
    Optional<Image> findFirstByProductVariantIdOrderByIsPrimaryDescIdAsc(Integer productVariantId);
    Optional<Image> findFirstByProduct_Brand_IdAndIsPrimaryTrueOrderByIdAsc(Integer brandId);

    Optional<Image> findFirstByProduct_Brand_IdOrderByIdAsc(Integer brandId);
}
