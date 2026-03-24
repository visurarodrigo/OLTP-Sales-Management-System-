package com.oltp.service;

import com.oltp.entity.*;
import com.oltp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final SalesRepository salesRepository;

    private final DimProductRepository dimProductRepository;
    private final DimLocationRepository dimLocationRepository;
    private final DimDateRepository dimDateRepository;
    private final FactSalesRepository factSalesRepository;
    private final StageSalesRepository stageSalesRepository;
    private final SalesDatamartDailyRepository salesDatamartDailyRepository;

    @Transactional
    public void rebuildWarehouse() {
        loadSalesToStaging();
        rebuildStarSchemaFromStaging();
        refreshSalesDatamart();
    }

    @Transactional
    public void loadSalesToStaging() {
        stageSalesRepository.deleteAllInBatch();

        List<Sales> sales = salesRepository.findAll();
        LocalDateTime loadedAt = LocalDateTime.now();
        for (Sales sale : sales) {
            StageSales stageSales = new StageSales();
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

        salesDatamartDailyRepository.saveAll(martByKey.values());
    }
}
