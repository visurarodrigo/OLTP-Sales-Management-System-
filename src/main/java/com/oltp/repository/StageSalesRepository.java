package com.oltp.repository;

import com.oltp.entity.StageSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StageSalesRepository extends JpaRepository<StageSales, Long> {

	Optional<StageSales> findTopByOrderByLoadedAtDesc();
}
