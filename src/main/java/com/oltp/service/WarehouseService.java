package com.oltp.service;

import com.oltp.dto.SalesRankingRow;
import com.oltp.dto.WarehousePipelineStatusResponse;
import com.oltp.dto.WarehouseReconciliationResponse;
import com.oltp.entity.DimDate;
import com.oltp.entity.DimLocation;
import com.oltp.entity.DimProduct;
import com.oltp.entity.FactSales;
import com.oltp.entity.Location;
import com.oltp.entity.Product;
import com.oltp.entity.Sales;
import com.oltp.entity.SalesDatamartDaily;
import com.oltp.entity.StageSales;
import com.oltp.entity.WarehousePipelineState;
import com.oltp.repository.DimDateRepository;
import com.oltp.repository.DimLocationRepository;
import com.oltp.repository.DimProductRepository;
import com.oltp.repository.FactSalesRepository;
import com.oltp.repository.LocationRepository;
import com.oltp.repository.ProductRepository;
import com.oltp.repository.SalesDatamartDailyRepository;
import com.oltp.repository.SalesRepository;
import com.oltp.repository.StageSalesRepository;
import com.oltp.repository.WarehousePipelineStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private static final String PIPELINE_STATE_KEY = "sales_warehouse";

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final SalesRepository salesRepository;

    private final DimProductRepository dimProductRepository;
    private final DimLocationRepository dimLocationRepository;
    private final DimDateRepository dimDateRepository;
    private final FactSalesRepository factSalesRepository;
    private final StageSalesRepository stageSalesRepository;
    private final SalesDatamartDailyRepository salesDatamartDailyRepository;
    private final WarehousePipelineStateRepository warehousePipelineStateRepository;

    @Transactional
    public void rebuildWarehouse() {
        loadSalesToStaging();
        rebuildStarSchemaFromStaging();
        refreshSalesDatamart();
        updateWatermarkFromSales();
    }

    @Transactional
    public long runIncrementalWarehousePipeline() {
        long changedRows = loadIncrementalSalesToStaging();
        if (changedRows == 0) {
            return 0;
        }

        rebuildStarSchemaFromStaging();
        refreshSalesDatamart();
        return changedRows;
    }

    @Transactional
    public void loadSalesToStaging() {
        stageSalesRepository.deleteAllInBatch();

        List<Sales> sales = salesRepository.findAll();
        LocalDateTime loadedAt = LocalDateTime.now();
        for (Sales sale : sales) {
            upsertStageRow(sale, loadedAt);
        }
    }

    @Transactional
    public long loadIncrementalSalesToStaging() {
        LocalDateTime lastWatermark = getCurrentWatermark();
        List<Sales> changedSales = (lastWatermark == null)
                ? salesRepository.findAll()
                : salesRepository.findByUpdatedAtAfterOrderByUpdatedAtAsc(lastWatermark);

        if (changedSales.isEmpty()) {
            return 0;
        }

        LocalDateTime loadedAt = LocalDateTime.now();
        LocalDateTime maxUpdatedAt = lastWatermark;

        for (Sales sale : changedSales) {
            upsertStageRow(sale, loadedAt);
            if (sale.getUpdatedAt() != null && (maxUpdatedAt == null || sale.getUpdatedAt().isAfter(maxUpdatedAt))) {
                maxUpdatedAt = sale.getUpdatedAt();
            }
        }

        saveWatermark(maxUpdatedAt);
        return changedSales.size();
    }

    @Transactional
    public void rebuildStarSchemaFromStaging() {
        factSalesRepository.deleteAllInBatch();
        dimDateRepository.deleteAllInBatch();
        dimProductRepository.deleteAllInBatch();
        dimLocationRepository.deleteAllInBatch();

        Map<Long, DimProduct> dimProductMap = new HashMap<>();
        for (Product product : productRepository.findAll()) {
            DimProduct dimProduct = new DimProduct();
            dimProduct.setSourceProductId(product.getProductId());
            dimProduct.setSku(product.getSku());
            dimProduct.setProductName(product.getProductName());
            dimProduct.setCategory(product.getCategory());
            dimProduct.setSubCategory(product.getSubCategory());
            dimProduct.setBrand(product.getBrand());
            dimProductMap.put(product.getProductId(), dimProductRepository.save(dimProduct));
        }

        Map<Long, DimLocation> dimLocationMap = new HashMap<>();
        for (Location location : locationRepository.findAll()) {
            DimLocation dimLocation = new DimLocation();
            dimLocation.setSourceLocationId(location.getLocationId());
            dimLocation.setStoreCode(location.getStoreCode());
            dimLocation.setStoreName(location.getStoreName());
            dimLocation.setLocationType(location.getLocationType());
            dimLocation.setCity(location.getCity());
            dimLocation.setState(location.getState());
            dimLocation.setCountry(location.getCountry());
            dimLocationMap.put(location.getLocationId(), dimLocationRepository.save(dimLocation));
        }

        WeekFields weekFields = WeekFields.of(Locale.US);
        Map<LocalDate, DimDate> dimDateMap = new HashMap<>();
        List<StageSales> stagedSales = stageSalesRepository.findAll();
        for (StageSales stagedSale : stagedSales) {
            LocalDate date = stagedSale.getSaleDay();
            DimDate dimDate = dimDateMap.get(date);
            if (dimDate == null) {
                dimDate = new DimDate();
                dimDate.setFullDate(date);
                dimDate.setDay(date.getDayOfMonth());
                dimDate.setMonth(date.getMonthValue());
                dimDate.setYear(date.getYear());
                dimDate.setQuarter(((date.getMonthValue() - 1) / 3) + 1);
                dimDate.setWeekOfYear(date.get(weekFields.weekOfWeekBasedYear()));
                dimDate = dimDateRepository.save(dimDate);
                dimDateMap.put(date, dimDate);
            }

            FactSales factSales = new FactSales();
            factSales.setSourceSaleId(stagedSale.getSourceSaleId());
            factSales.setDimProduct(dimProductMap.get(stagedSale.getSourceProductId()));
            factSales.setDimLocation(dimLocationMap.get(stagedSale.getSourceLocationId()));
            factSales.setDimDate(dimDate);
            factSales.setQuantity(stagedSale.getQuantity());
            factSales.setTotalAmount(stagedSale.getTotalAmount());
            factSales.setTransactionCount(1);
            factSalesRepository.save(factSales);
        }
    }

    @Transactional
    public void refreshSalesDatamart() {
        salesDatamartDailyRepository.deleteAllInBatch();

        List<FactSales> factRows = factSalesRepository.findAll();
        Map<String, SalesDatamartDaily> martByKey = new HashMap<>();

        for (FactSales fact : factRows) {
            DimDate dimDate = fact.getDimDate();
            DimProduct dimProduct = fact.getDimProduct();
            DimLocation dimLocation = fact.getDimLocation();

            String key = dimDate.getFullDate() + "|" + dimProduct.getSourceProductId() + "|" + dimLocation.getSourceLocationId();
            SalesDatamartDaily row = martByKey.computeIfAbsent(key, ignored -> {
                SalesDatamartDaily mart = new SalesDatamartDaily();
                mart.setSaleDate(dimDate.getFullDate());
                mart.setSourceProductId(dimProduct.getSourceProductId());
                mart.setProductName(dimProduct.getProductName());
                mart.setCategory(dimProduct.getCategory());
                mart.setSourceLocationId(dimLocation.getSourceLocationId());
                mart.setStoreName(dimLocation.getStoreName());
                mart.setCity(dimLocation.getCity());
                mart.setState(dimLocation.getState());
                mart.setTotalQuantity(0L);
                mart.setTotalRevenue(BigDecimal.ZERO);
                mart.setTotalTransactions(0L);
                return mart;
            });

            long quantity = fact.getQuantity() == null ? 0L : fact.getQuantity();
            BigDecimal revenue = fact.getTotalAmount() == null ? BigDecimal.ZERO : fact.getTotalAmount();
            long transactions = fact.getTransactionCount() == null ? 0L : fact.getTransactionCount();

            row.setTotalQuantity(row.getTotalQuantity() + quantity);
            row.setTotalRevenue(row.getTotalRevenue().add(revenue));
            row.setTotalTransactions(row.getTotalTransactions() + transactions);
        }

        salesDatamartDailyRepository.saveAll(new ArrayList<>(martByKey.values()));
    }

    @Transactional(readOnly = true)
    public WarehousePipelineStatusResponse getPipelineStatus() {
        LocalDateTime lastLoadedAt = stageSalesRepository.findTopByOrderByLoadedAtDesc()
                .map(StageSales::getLoadedAt)
                .orElse(null);
        LocalDateTime lastSuccessfulSourceUpdateAt = getCurrentWatermark();

        return WarehousePipelineStatusResponse.builder()
                .stagingRows(stageSalesRepository.count())
                .dimProductRows(dimProductRepository.count())
                .dimLocationRows(dimLocationRepository.count())
                .dimDateRows(dimDateRepository.count())
                .factRows(factSalesRepository.count())
                .datamartRows(salesDatamartDailyRepository.count())
                .lastStagingLoadedAt(lastLoadedAt)
                .lastSuccessfulSourceUpdateAt(lastSuccessfulSourceUpdateAt)
                .build();
    }

    @Transactional(readOnly = true)
    public WarehouseReconciliationResponse reconcile(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Sales> oltpRows = salesRepository.findBySaleDateBetween(startDateTime, endDateTime);
        List<FactSales> factRows = factSalesRepository.findByDimDate_FullDateBetween(startDate, endDate);
        List<SalesDatamartDaily> datamartRows = salesDatamartDailyRepository.findBySaleDateBetween(startDate, endDate);

        long oltpQuantity = oltpRows.stream().map(Sales::getQuantity).filter(q -> q != null).mapToLong(Integer::longValue).sum();
        BigDecimal oltpRevenue = oltpRows.stream().map(Sales::getTotalAmount).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        long oltpTransactions = oltpRows.size();

        long factQuantity = factRows.stream().map(FactSales::getQuantity).filter(q -> q != null).mapToLong(Integer::longValue).sum();
        BigDecimal factRevenue = factRows.stream().map(FactSales::getTotalAmount).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        long factTransactions = factRows.stream().map(FactSales::getTransactionCount).filter(t -> t != null).mapToLong(Integer::longValue).sum();

        long datamartQuantity = datamartRows.stream().map(SalesDatamartDaily::getTotalQuantity).filter(q -> q != null).mapToLong(Long::longValue).sum();
        BigDecimal datamartRevenue = datamartRows.stream().map(SalesDatamartDaily::getTotalRevenue).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        long datamartTransactions = datamartRows.stream().map(SalesDatamartDaily::getTotalTransactions).filter(t -> t != null).mapToLong(Long::longValue).sum();

        return WarehouseReconciliationResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .oltpQuantity(oltpQuantity)
                .oltpRevenue(oltpRevenue)
                .oltpTransactions(oltpTransactions)
                .factQuantity(factQuantity)
                .factRevenue(factRevenue)
                .factTransactions(factTransactions)
                .datamartQuantity(datamartQuantity)
                .datamartRevenue(datamartRevenue)
                .datamartTransactions(datamartTransactions)
                .quantityBalanced(oltpQuantity == factQuantity && factQuantity == datamartQuantity)
                .revenueBalanced(oltpRevenue.compareTo(factRevenue) == 0 && factRevenue.compareTo(datamartRevenue) == 0)
                .transactionBalanced(oltpTransactions == factTransactions && factTransactions == datamartTransactions)
                .build();
    }

    @Transactional(readOnly = true)
    public List<SalesDatamartDaily> getDatamartDaily(LocalDate startDate,
                                                     LocalDate endDate,
                                                     Long productId,
                                                     Long locationId) {
        if (productId != null && locationId != null) {
            return salesDatamartDailyRepository.findBySaleDateBetweenAndSourceProductIdAndSourceLocationId(
                    startDate,
                    endDate,
                    productId,
                    locationId
            );
        }
        if (productId != null) {
            return salesDatamartDailyRepository.findBySaleDateBetweenAndSourceProductId(startDate, endDate, productId);
        }
        if (locationId != null) {
            return salesDatamartDailyRepository.findBySaleDateBetweenAndSourceLocationId(startDate, endDate, locationId);
        }
        return salesDatamartDailyRepository.findBySaleDateBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<SalesRankingRow> getTopProducts(LocalDate startDate, LocalDate endDate, Integer limit) {
        int topN = (limit == null || limit < 1) ? 10 : limit;
        return salesDatamartDailyRepository.findBySaleDateBetween(startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(
                        row -> row.getSourceProductId() + " - " + row.getProductName(),
                        Collectors.reducing(
                                SalesRankingRow.builder().label("").quantity(0).transactions(0).revenue(BigDecimal.ZERO).build(),
                                row -> SalesRankingRow.builder()
                                        .label(row.getSourceProductId() + " - " + row.getProductName())
                                        .quantity(row.getTotalQuantity() == null ? 0 : row.getTotalQuantity())
                                        .transactions(row.getTotalTransactions() == null ? 0 : row.getTotalTransactions())
                                        .revenue(row.getTotalRevenue() == null ? BigDecimal.ZERO : row.getTotalRevenue())
                                        .build(),
                                (a, b) -> SalesRankingRow.builder()
                                        .label(a.getLabel().isEmpty() ? b.getLabel() : a.getLabel())
                                        .quantity(a.getQuantity() + b.getQuantity())
                                        .transactions(a.getTransactions() + b.getTransactions())
                                        .revenue(a.getRevenue().add(b.getRevenue()))
                                        .build()
                        )
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(SalesRankingRow::getRevenue).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SalesRankingRow> getTopLocations(LocalDate startDate, LocalDate endDate, Integer limit) {
        int topN = (limit == null || limit < 1) ? 10 : limit;
        return salesDatamartDailyRepository.findBySaleDateBetween(startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(
                        row -> row.getSourceLocationId() + " - " + row.getStoreName() + " (" + row.getCity() + ")",
                        Collectors.reducing(
                                SalesRankingRow.builder().label("").quantity(0).transactions(0).revenue(BigDecimal.ZERO).build(),
                                row -> SalesRankingRow.builder()
                                        .label(row.getSourceLocationId() + " - " + row.getStoreName() + " (" + row.getCity() + ")")
                                        .quantity(row.getTotalQuantity() == null ? 0 : row.getTotalQuantity())
                                        .transactions(row.getTotalTransactions() == null ? 0 : row.getTotalTransactions())
                                        .revenue(row.getTotalRevenue() == null ? BigDecimal.ZERO : row.getTotalRevenue())
                                        .build(),
                                (a, b) -> SalesRankingRow.builder()
                                        .label(a.getLabel().isEmpty() ? b.getLabel() : a.getLabel())
                                        .quantity(a.getQuantity() + b.getQuantity())
                                        .transactions(a.getTransactions() + b.getTransactions())
                                        .revenue(a.getRevenue().add(b.getRevenue()))
                                        .build()
                        )
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(SalesRankingRow::getRevenue).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    private void upsertStageRow(Sales sale, LocalDateTime loadedAt) {
        StageSales stageSales = stageSalesRepository.findBySourceSaleId(sale.getSaleId())
                .orElseGet(StageSales::new);

        stageSales.setSourceSaleId(sale.getSaleId());
        stageSales.setSourceCustomerId(sale.getCustomer().getCustomerId());
        stageSales.setSourceProductId(sale.getProduct().getProductId());
        stageSales.setSourceLocationId(sale.getLocation().getLocationId());
        stageSales.setSaleDate(sale.getSaleDate());
        stageSales.setSaleDay(sale.getSaleDate().toLocalDate());
        stageSales.setQuantity(sale.getQuantity());
        stageSales.setTotalAmount(sale.getTotalAmount());
        stageSales.setLoadedAt(loadedAt);
        stageSalesRepository.save(stageSales);
    }

    private void updateWatermarkFromSales() {
        List<Sales> allSales = salesRepository.findAll();
        LocalDateTime maxUpdatedAt = allSales.stream()
                .map(Sales::getUpdatedAt)
                .filter(value -> value != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        saveWatermark(maxUpdatedAt);
    }

    private LocalDateTime getCurrentWatermark() {
        Optional<WarehousePipelineState> state = warehousePipelineStateRepository.findById(PIPELINE_STATE_KEY);
        return state.map(WarehousePipelineState::getLastSuccessfulSourceUpdateAt).orElse(null);
    }

    private void saveWatermark(LocalDateTime watermark) {
        WarehousePipelineState state = warehousePipelineStateRepository.findById(PIPELINE_STATE_KEY)
                .orElseGet(() -> new WarehousePipelineState(PIPELINE_STATE_KEY, null, LocalDateTime.now()));
        state.setLastSuccessfulSourceUpdateAt(watermark);
        state.setUpdatedAt(LocalDateTime.now());
        warehousePipelineStateRepository.save(state);
    }
}
