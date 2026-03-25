package com.oltp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseReconciliationResponse {

    private LocalDate startDate;
    private LocalDate endDate;

    private long oltpQuantity;
    private BigDecimal oltpRevenue;
    private long oltpTransactions;

    private long factQuantity;
    private BigDecimal factRevenue;
    private long factTransactions;

    private long datamartQuantity;
    private BigDecimal datamartRevenue;
    private long datamartTransactions;

    private boolean quantityBalanced;
    private boolean revenueBalanced;
    private boolean transactionBalanced;
}
