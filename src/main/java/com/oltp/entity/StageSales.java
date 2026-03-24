package com.oltp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stage_sales", indexes = {
        @Index(name = "idx_stage_sales_product", columnList = "source_product_id"),
        @Index(name = "idx_stage_sales_location", columnList = "source_location_id"),
        @Index(name = "idx_stage_sales_sale_day", columnList = "sale_day")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stage_sales_id")
    private Long stageSalesId;

    @Column(name = "source_sale_id", nullable = false, unique = true)
    private Long sourceSaleId;

    @Column(name = "source_customer_id", nullable = false)
    private Long sourceCustomerId;

    @Column(name = "source_product_id", nullable = false)
    private Long sourceProductId;

    @Column(name = "source_location_id", nullable = false)
    private Long sourceLocationId;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(name = "sale_day", nullable = false)
    private LocalDate saleDay;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "loaded_at", nullable = false)
    private LocalDateTime loadedAt;
}
