package com.oltp.repository;

import com.oltp.entity.WarehousePipelineState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehousePipelineStateRepository extends JpaRepository<WarehousePipelineState, String> {
}
