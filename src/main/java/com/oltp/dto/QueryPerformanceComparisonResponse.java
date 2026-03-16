package com.oltp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryPerformanceComparisonResponse {

    private Long productId;
    private Long locationId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private QueryModelPerformance oltp;
    private QueryModelPerformance dimensional;

    private double dimensionalImprovementPercent;
}
