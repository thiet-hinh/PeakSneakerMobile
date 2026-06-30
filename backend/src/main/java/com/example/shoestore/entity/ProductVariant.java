package com.example.shoestore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // 💡 THÊM IMPORT NÀY
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_variant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"product", "images"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "size", length = 20)
    private String size;

    @Builder.Default
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 💡 THÊM @JsonIgnore: Chặn Jackson quay ngược lại Product gây lỗi vòng lặp No Session
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_variant_product"))
    @JsonIgnore
    private Product product;

    // 💡 THÊM @JsonIgnore: Android đã có link ảnh chính từ DTO, ẩn list này để tránh lỗi nạp chậm
    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<Image> images = new ArrayList<>();

    // 💡 THÊM @JsonIgnore: Màn chi tiết sản phẩm không cần thông tin lịch sử đơn hàng của biến thể này
    @OneToMany(mappedBy = "productVariant")
    @Builder.Default
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}