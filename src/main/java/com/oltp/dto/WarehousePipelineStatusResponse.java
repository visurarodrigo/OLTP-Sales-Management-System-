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
public class WarehousePipelineStatusResponse {

    private long stagingRows;
    private long dimProductRows;
    private long dimLocationRows;
    private long dimDateRows;
    private long factRows;
    private long datamartRows;
    private LocalDateTime lastStagingLoadedAt;
}
