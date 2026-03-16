package com.oltp.repository;

import com.oltp.entity.DimLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DimLocationRepository extends JpaRepository<DimLocation, Long> {

    Optional<DimLocation> findBySourceLocationId(Long sourceLocationId);
}
