package com.oltp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "dim_product", indexes = {
        @Index(name = "idx_dim_product_source_id", columnList = "source_product_id"),
        @Index(name = "idx_dim_product_category", columnList = "category")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dim_product_id")
    private Long dimProductId;

    @Column(name = "source_product_id", nullable = false, unique = true)
    private Long sourceProductId;

    @Column(name = "sku", nullable = false, length = 50)
    private String sku;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "sub_category", length = 50)
    private String subCategory;

    @Column(name = "brand", length = 100)
    private String brand;
}
