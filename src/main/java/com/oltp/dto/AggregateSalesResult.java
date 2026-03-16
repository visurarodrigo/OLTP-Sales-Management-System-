package com.oltp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregateSalesResult {

    private long totalQuantity;
    private BigDecimal totalRevenue;
    private long totalTransactions;
}
