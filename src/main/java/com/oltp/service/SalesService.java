package com.oltp.service;

import com.oltp.dto.ProductLocationSalesAnalytics;
import com.oltp.dto.SalesRankingRow;
import com.oltp.entity.Sales;
import com.oltp.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesService {

    private final SalesRepository salesRepository;
    private final ProductService productService;

    @Transactional
    public Sales createSale(Sales sale) {
        // Calculate amounts
        BigDecimal subtotal = sale.getUnitPrice().multiply(new BigDecimal(sale.getQuantity()));
        sale.setSubtotal(subtotal);
        
        BigDecimal discountAmount = sale.getDiscountAmount() != null ? sale.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal taxableAmount = subtotal.subtract(discountAmount);
        BigDecimal taxAmount = taxableAmount.multiply(new BigDecimal("0.10")); // 10% tax
        sale.setTaxAmount(taxAmount);
        
        BigDecimal totalAmount = taxableAmount.add(taxAmount);
        sale.setTotalAmount(totalAmount);
        
        // Update product stock
        productService.updateStock(sale.getProduct().getProductId(), sale.getQuantity());
        
        return salesRepository.save(sale);
    }

    public Optional<Sales> getSaleById(Long id) {
        return salesRepository.findById(id);
    }

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    public List<Sales> getSalesByCustomer(Long customerId) {
        return salesRepository.findByCustomer_CustomerId(customerId);
    }

    public List<Sales> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return salesRepository.findBySaleDateBetween(startDate, endDate);
    }

        public List<Sales> getSalesByProductLocationAndDateRange(Long productId,
                                     Long locationId,
                                     LocalDateTime startDate,
                                     LocalDateTime endDate) {
        return salesRepository.findByProduct_ProductIdAndLocation_LocationIdAndSaleDateBetween(
            productId,
            locationId,
            startDate,
            endDate
        );
        }

        public List<Sales> getSalesByAdvancedFilters(Long productId,
                             Long locationId,
                             LocalDateTime startDate,
                             LocalDateTime endDate,
                             String paymentMethod,
                             String orderStatus,
                             Integer minQuantity,
                             Integer maxQuantity) {
        return salesRepository.findAll().stream()
            .filter(sale -> productId == null ||
                (sale.getProduct() != null && Objects.equals(sale.getProduct().getProductId(), productId)))
            .filter(sale -> locationId == null ||
                (sale.getLocation() != null && Objects.equals(sale.getLocation().getLocationId(), locationId)))
            .filter(sale -> startDate == null ||
                (sale.getSaleDate() != null && !sale.getSaleDate().isBefore(startDate)))
            .filter(sale -> endDate == null ||
                (sale.getSaleDate() != null && !sale.getSaleDate().isAfter(endDate)))
            .filter(sale -> paymentMethod == null || paymentMethod.trim().isEmpty() ||
                paymentMethod.equalsIgnoreCase(sale.getPaymentMethod()))
            .filter(sale -> orderStatus == null || orderStatus.trim().isEmpty() ||
                orderStatus.equalsIgnoreCase(sale.getOrderStatus()))
            .filter(sale -> minQuantity == null ||
                (sale.getQuantity() != null && sale.getQuantity() >= minQuantity))
            .filter(sale -> maxQuantity == null ||
                (sale.getQuantity() != null && sale.getQuantity() <= maxQuantity))
            .sorted(Comparator.comparing(Sales::getSaleDate, Comparator.nullsLast(Comparator.reverseOrder())))
            .collect(Collectors.toList());
        }

        public List<Sales> getTopSalesByAmount(List<Sales> sales, int limit) {
        int safeLimit = Math.max(1, limit);
        return sales.stream()
            .sorted(Comparator.comparing(
                    Sales::getTotalAmount,
                    Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Sales::getSaleDate, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(safeLimit)
            .collect(Collectors.toList());
        }

        public List<SalesRankingRow> getTopProductsByQuantity(List<Sales> sales, int limit) {
        int safeLimit = Math.max(1, limit);
        Map<String, List<Sales>> grouped = sales.stream()
            .filter(sale -> sale.getProduct() != null)
            .collect(Collectors.groupingBy(sale -> sale.getProduct().getProductName()));

        return buildRankingRows(grouped, safeLimit);
        }

        public List<SalesRankingRow> getTopLocationsByQuantity(List<Sales> sales, int limit) {
        int safeLimit = Math.max(1, limit);
        Map<String, List<Sales>> grouped = sales.stream()
            .filter(sale -> sale.getLocation() != null)
            .collect(Collectors.groupingBy(sale -> {
                String city = sale.getLocation().getCity() != null ? sale.getLocation().getCity() : "Unknown";
                String state = sale.getLocation().getState() != null ? sale.getLocation().getState() : "NA";
                return city + ", " + state;
            }));

        return buildRankingRows(grouped, safeLimit);
        }

        private List<SalesRankingRow> buildRankingRows(Map<String, List<Sales>> groupedSales, int limit) {
        List<SalesRankingRow> rows = new ArrayList<>();
        for (Map.Entry<String, List<Sales>> entry : groupedSales.entrySet()) {
            List<Sales> grouped = entry.getValue();
            long transactions = grouped.size();
            long quantity = grouped.stream()
                .map(Sales::getQuantity)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum();
            BigDecimal revenue = grouped.stream()
                .map(Sales::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            rows.add(SalesRankingRow.builder()
                .label(entry.getKey())
                .transactions(transactions)
                .quantity(quantity)
                .revenue(revenue)
                .build());
        }

        return rows.stream()
            .sorted(Comparator.comparing(SalesRankingRow::getQuantity, Comparator.reverseOrder())
                .thenComparing(SalesRankingRow::getRevenue, Comparator.reverseOrder()))
            .limit(limit)
            .collect(Collectors.toList());
        }

        public ProductLocationSalesAnalytics getProductLocationAnalytics(Long productId,
                                         Long locationId,
                                         LocalDateTime startDate,
                                         LocalDateTime endDate) {
        List<Sales> sales = getSalesByProductLocationAndDateRange(productId, locationId, startDate, endDate);

        long totalTransactions = sales.size();
        long totalQuantity = sales.stream()
            .map(Sales::getQuantity)
            .filter(Objects::nonNull)
            .mapToLong(Integer::longValue)
            .sum();

        BigDecimal totalRevenue = sales.stream()
            .map(Sales::getTotalAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        int maxSingleSaleQuantity = sales.stream()
            .map(Sales::getQuantity)
            .filter(Objects::nonNull)
            .max(Integer::compareTo)
            .orElse(0);

        Map<LocalDate, List<Sales>> salesByDay = sales.stream()
            .filter(sale -> sale.getSaleDate() != null)
            .collect(Collectors.groupingBy(sale -> sale.getSaleDate().toLocalDate()));

        long maxDailyTransactionCount = salesByDay.values().stream()
            .mapToLong(List::size)
            .max()
            .orElse(0L);

        long maxDailyQuantity = salesByDay.values().stream()
            .mapToLong(daySales -> daySales.stream()
                .map(Sales::getQuantity)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum())
            .max()
            .orElse(0L);

        return ProductLocationSalesAnalytics.builder()
                .productId(productId)
                .locationId(locationId)
                .startDate(startDate)
                .endDate(endDate)
                .totalTransactions(totalTransactions)
                .totalQuantity(totalQuantity)
                .totalRevenue(totalRevenue)
                .maxSingleSaleQuantity(maxSingleSaleQuantity)
                .maxDailyTransactionCount(maxDailyTransactionCount)
                .maxDailyQuantity(maxDailyQuantity)
                .build();
    }

    public BigDecimal calculateRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return salesRepository.calculateTotalRevenue(startDate, endDate);
    }

    public void deleteSale(Long id) {
        salesRepository.deleteById(id);
    }
}
