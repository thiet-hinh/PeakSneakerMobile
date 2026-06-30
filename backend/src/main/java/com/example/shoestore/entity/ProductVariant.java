package com.example.shoestore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_variant_product"))
    @JsonIgnore
    private Product product;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<Image> images = new ArrayList<>();

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