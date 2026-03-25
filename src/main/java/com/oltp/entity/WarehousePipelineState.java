package com.oltp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_pipeline_state")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehousePipelineState {

    @Id
    @Column(name = "state_key", length = 50)
    private String stateKey;

    @Column(name = "last_successful_source_update_at")
    private LocalDateTime lastSuccessfulSourceUpdateAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
