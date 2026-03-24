package com.oltp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales_datamart_daily", indexes = {
        @Index(name = "idx_sales_mart_day", columnList = "sale_date"),
        @Index(name = "idx_sales_mart_product", columnList = "source_product_id"),
        @Index(name = "idx_sales_mart_location", columnList = "source_location_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_sales_mart_day_product_location", columnNames = {"sale_date", "source_product_id", "source_location_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesDatamartDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_mart_id")
    private Long salesMartId;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @Column(name = "source_product_id", nullable = false)
    private Long sourceProductId;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "source_location_id", nullable = false)
    private Long sourceLocationId;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @Column(name = "total_quantity", nullable = false)
    private Long totalQuantity;

    @Column(name = "total_revenue", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "total_transactions", nullable = false)
    private Long totalTransactions;
}
