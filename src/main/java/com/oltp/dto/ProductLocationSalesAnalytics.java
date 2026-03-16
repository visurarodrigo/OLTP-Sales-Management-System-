package com.oltp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLocationSalesAnalytics {

    private Long productId;
    private Long locationId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private long totalTransactions;
    private long totalQuantity;
    private BigDecimal totalRevenue;

    private int maxSingleSaleQuantity;
    private long maxDailyTransactionCount;
    private long maxDailyQuantity;
}
