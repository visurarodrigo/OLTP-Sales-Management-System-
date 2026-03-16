package com.oltp.service;

import com.oltp.dto.AggregateSalesResult;
import com.oltp.dto.QueryModelPerformance;
import com.oltp.dto.QueryPerformanceComparisonResponse;
import com.oltp.entity.FactSales;
import com.oltp.entity.Sales;
import com.oltp.repository.FactSalesRepository;
import com.oltp.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryPerformanceService {

    private final SalesRepository salesRepository;
    private final FactSalesRepository factSalesRepository;

    public QueryPerformanceComparisonResponse compareOltpVsDimensional(Long productId,
                                                                       Long locationId,
                                                                       LocalDateTime startDate,
                                                                       LocalDateTime endDate,
                                                                       Integer runs) {
        int benchmarkRuns = (runs == null || runs < 1) ? 20 : runs;

        List<Sales> oltpRows = salesRepository.findByProduct_ProductIdAndLocation_LocationIdAndSaleDateBetween(
                productId,
                locationId,
                startDate,
                endDate
        );

        List<FactSales> dimensionalRows = factSalesRepository
                .findByDimProduct_SourceProductIdAndDimLocation_SourceLocationIdAndDimDate_FullDateBetween(
                        productId,
                        locationId,
                        startDate.toLocalDate(),
                        endDate.toLocalDate()
                );

        AggregateSalesResult oltpResult = aggregateOltpRows(oltpRows);
        AggregateSalesResult dimensionalResult = aggregateDimensionalRows(dimensionalRows);

        long oltpStart = System.nanoTime();
        for (int i = 0; i < benchmarkRuns; i++) {
            salesRepository.findByProduct_ProductIdAndLocation_LocationIdAndSaleDateBetween(
                    productId,
                    locationId,
                    startDate,
                    endDate
            );
        }
        long oltpDurationNs = System.nanoTime() - oltpStart;

        long dimensionalStart = System.nanoTime();
        for (int i = 0; i < benchmarkRuns; i++) {
            factSalesRepository.findByDimProduct_SourceProductIdAndDimLocation_SourceLocationIdAndDimDate_FullDateBetween(
                    productId,
                    locationId,
                    startDate.toLocalDate(),
                    endDate.toLocalDate()
            );
        }
        long dimensionalDurationNs = System.nanoTime() - dimensionalStart;

        double oltpAvgMs = nsToMillis(oltpDurationNs) / benchmarkRuns;
        double dimensionalAvgMs = nsToMillis(dimensionalDurationNs) / benchmarkRuns;

        double improvementPercent = 0.0;
        if (oltpAvgMs > 0.0) {
            improvementPercent = ((oltpAvgMs - dimensionalAvgMs) / oltpAvgMs) * 100.0;
        }

        return QueryPerformanceComparisonResponse.builder()
                .productId(productId)
                .locationId(locationId)
                .startDate(startDate)
                .endDate(endDate)
                .oltp(QueryModelPerformance.builder()
                        .model("OLTP")
                        .runs(benchmarkRuns)
                        .averageMillis(oltpAvgMs)
                        .result(oltpResult)
                        .build())
                .dimensional(QueryModelPerformance.builder()
                        .model("DIMENSIONAL")
                        .runs(benchmarkRuns)
                        .averageMillis(dimensionalAvgMs)
                        .result(dimensionalResult)
                        .build())
                .dimensionalImprovementPercent(improvementPercent)
                .build();
    }

    private AggregateSalesResult aggregateOltpRows(List<Sales> rows) {
        long totalQuantity = rows.stream()
                .map(Sales::getQuantity)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum();
        BigDecimal totalRevenue = rows.stream()
                .map(Sales::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return AggregateSalesResult.builder()
                .totalQuantity(totalQuantity)
                .totalRevenue(totalRevenue)
                .totalTransactions(rows.size())
                .build();
    }

    private AggregateSalesResult aggregateDimensionalRows(List<FactSales> rows) {
        long totalQuantity = rows.stream()
                .map(FactSales::getQuantity)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum();
        BigDecimal totalRevenue = rows.stream()
                .map(FactSales::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long totalTransactions = rows.stream()
                .map(FactSales::getTransactionCount)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum();

        return AggregateSalesResult.builder()
                .totalQuantity(totalQuantity)
                .totalRevenue(totalRevenue)
                .totalTransactions(totalTransactions)
                .build();
    }

    private double nsToMillis(long nanos) {
        return nanos / 1_000_000.0;
    }
}
