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

    @PostMapping("/warehouse/rebuild")
    public ResponseEntity<String> rebuildWarehouse() {
        warehouseService.rebuildWarehouse();
        return ResponseEntity.ok("Warehouse tables rebuilt from OLTP data.");
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
