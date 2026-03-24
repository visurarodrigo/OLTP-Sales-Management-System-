package com.oltp.controller;

import com.oltp.dto.QueryPerformanceComparisonResponse;
import com.oltp.service.QueryPerformanceService;
import com.oltp.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/benchmark")
@RequiredArgsConstructor
public class BenchmarkController {

    private final QueryPerformanceService queryPerformanceService;
    private final WarehouseService warehouseService;

    @RequestMapping(value = "/warehouse/rebuild", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> rebuildWarehouse() {
        warehouseService.rebuildWarehouse();
        return ResponseEntity.ok("Phase 3 pipeline completed: staging loaded, star schema rebuilt, datamart refreshed.");
    }

    @PostMapping("/warehouse/staging/load")
    public ResponseEntity<String> loadWarehouseStaging() {
        warehouseService.loadSalesToStaging();
        return ResponseEntity.ok("Staging area loaded from OLTP sales.");
    }

    @PostMapping("/warehouse/star/rebuild")
    public ResponseEntity<String> rebuildWarehouseStarSchema() {
        warehouseService.rebuildStarSchemaFromStaging();
        return ResponseEntity.ok("Star schema rebuilt from staging area.");
    }

    @PostMapping("/warehouse/datamart/refresh")
    public ResponseEntity<String> refreshSalesDatamart() {
        warehouseService.refreshSalesDatamart();
        return ResponseEntity.ok("Sales datamart refreshed from fact table.");
    }

    @GetMapping("/sales-compare")
    public ResponseEntity<QueryPerformanceComparisonResponse> compareSalesQueryPerformance(
            @RequestParam Long productId,
            @RequestParam Long locationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "20") Integer runs) {
        return ResponseEntity.ok(
                queryPerformanceService.compareOltpVsDimensional(productId, locationId, startDate, endDate, runs)
        );
    }
}
