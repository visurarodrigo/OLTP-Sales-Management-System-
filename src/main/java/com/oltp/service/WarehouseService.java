package com.oltp.service;

import com.oltp.entity.*;
import com.oltp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Transactional
    public void rebuildWarehouse() {
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
        List<Sales> sales = salesRepository.findAll();
        for (Sales sale : sales) {
            LocalDate date = sale.getSaleDate().toLocalDate();
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
            factSales.setSourceSaleId(sale.getSaleId());
            factSales.setDimProduct(dimProductMap.get(sale.getProduct().getProductId()));
            factSales.setDimLocation(dimLocationMap.get(sale.getLocation().getLocationId()));
            factSales.setDimDate(dimDate);
            factSales.setQuantity(sale.getQuantity());
            factSales.setTotalAmount(sale.getTotalAmount());
            factSales.setTransactionCount(1);
            factSalesRepository.save(factSales);
        }
    }
}
