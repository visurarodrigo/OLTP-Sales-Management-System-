package com.oltp.repository;

import com.oltp.entity.DimProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DimProductRepository extends JpaRepository<DimProduct, Long> {

    Optional<DimProduct> findBySourceProductId(Long sourceProductId);
}
