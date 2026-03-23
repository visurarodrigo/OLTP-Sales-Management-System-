package com.oltp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fact_sales", indexes = {
        @Index(name = "idx_fact_sales_product", columnList = "dim_product_id"),
        @Index(name = "idx_fact_sales_location", columnList = "dim_location_id"),
        @Index(name = "idx_fact_sales_date", columnList = "dim_date_id"),
        @Index(name = "idx_fact_sales_product_loc_date", columnList = "dim_product_id, dim_location_id, dim_date_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fact_sales_id")
    private Long factSalesId;

    @Column(name = "source_sale_id", nullable = false, unique = true)
    private Long sourceSaleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dim_product_id", nullable = false)
    private DimProduct dimProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dim_location_id", nullable = false)
    private DimLocation dimLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dim_date_id", nullable = false)
    private DimDate dimDate;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "transaction_count", nullable = false)
    private Integer transactionCount;
}
