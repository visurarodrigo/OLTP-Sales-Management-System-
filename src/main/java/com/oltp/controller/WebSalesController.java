package com.oltp.controller;

import com.oltp.dto.ProductLocationSalesAnalytics;
import com.oltp.dto.SalesRankingRow;
import com.oltp.entity.Sales;
import com.oltp.service.LocationService;
import com.oltp.service.ProductService;
import com.oltp.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/sales")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebSalesController {

    private final SalesService salesService;
    private final ProductService productService;
    private final LocationService locationService;

    @GetMapping
    public String listSales(Model model,
                            @RequestParam(required = false) Long productId,
                            @RequestParam(required = false) Long locationId,
                            @RequestParam(required = false)
                            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
                            @RequestParam(required = false)
                            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate,
                            @RequestParam(required = false) String paymentMethod,
                            @RequestParam(required = false) String orderStatus,
                            @RequestParam(required = false) Integer minQuantity,
                            @RequestParam(required = false) Integer maxQuantity,
                            @RequestParam(defaultValue = "CUSTOM") String period,
                            @RequestParam(defaultValue = "ALL") String mode,
                            @RequestParam(defaultValue = "10") Integer limit) {
        LocalDateTime resolvedStartDate = startDate;
        LocalDateTime resolvedEndDate = endDate;
        LocalDateTime now = LocalDateTime.now();

        if ("TODAY".equalsIgnoreCase(period)) {
            LocalDate today = now.toLocalDate();
            resolvedStartDate = today.atStartOfDay();
            resolvedEndDate = now;
        } else if ("LAST_7_DAYS".equalsIgnoreCase(period)) {
            resolvedStartDate = now.minusDays(7);
            resolvedEndDate = now;
        } else if ("LAST_30_DAYS".equalsIgnoreCase(period)) {
            resolvedStartDate = now.minusDays(30);
            resolvedEndDate = now;
        }

        int resolvedLimit = (limit == null || limit < 1) ? 10 : limit;

        List<Sales> filteredSales = salesService.getSalesByAdvancedFilters(
                productId,
                locationId,
                resolvedStartDate,
                resolvedEndDate,
                paymentMethod,
                orderStatus,
                minQuantity,
                maxQuantity
        );

        List<Sales> sales = filteredSales;
        List<SalesRankingRow> rankingRows = null;
        String rankingTitle = null;

        if ("TOP_SALES".equalsIgnoreCase(mode)) {
            sales = salesService.getTopSalesByAmount(filteredSales, resolvedLimit);
        } else if ("TOP_PRODUCTS".equalsIgnoreCase(mode)) {
            rankingRows = salesService.getTopProductsByQuantity(filteredSales, resolvedLimit);
            rankingTitle = "Top Products";
        } else if ("TOP_LOCATIONS".equalsIgnoreCase(mode)) {
            rankingRows = salesService.getTopLocationsByQuantity(filteredSales, resolvedLimit);
            rankingTitle = "Top Locations";
        }

        boolean hasAnalyticsFilters = productId != null && locationId != null && resolvedStartDate != null && resolvedEndDate != null;
        ProductLocationSalesAnalytics analytics = null;
        if (hasAnalyticsFilters) {
            analytics = salesService.getProductLocationAnalytics(productId, locationId, resolvedStartDate, resolvedEndDate);
        }

        model.addAttribute("sales", sales);

        BigDecimal totalRevenue = filteredSales.stream()
                .map(sale -> sale.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("hasFilters", !filteredSales.isEmpty());
        model.addAttribute("analytics", analytics);
        model.addAttribute("hasAnalyticsFilters", hasAnalyticsFilters);
        model.addAttribute("rankingRows", rankingRows);
        model.addAttribute("rankingTitle", rankingTitle);
        model.addAttribute("selectedPaymentMethod", paymentMethod);
        model.addAttribute("selectedOrderStatus", orderStatus);
        model.addAttribute("selectedMinQuantity", minQuantity);
        model.addAttribute("selectedMaxQuantity", maxQuantity);
        model.addAttribute("selectedPeriod", period);
        model.addAttribute("selectedMode", mode);
        model.addAttribute("selectedLimit", resolvedLimit);
        model.addAttribute("selectedProductId", productId);
        model.addAttribute("selectedLocationId", locationId);
        model.addAttribute("startDate", resolvedStartDate);
        model.addAttribute("endDate", resolvedEndDate);

        return "sales";
    }
}
