package com.oltp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryModelPerformance {

    private String model;
    private int runs;
    private double averageMillis;
    private AggregateSalesResult result;
}
