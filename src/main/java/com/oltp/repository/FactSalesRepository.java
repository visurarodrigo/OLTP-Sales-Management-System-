package com.oltp.repository;

import com.oltp.entity.FactSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FactSalesRepository extends JpaRepository<FactSales, Long> {

        List<FactSales> findByDimProduct_SourceProductIdAndDimLocation_SourceLocationIdAndDimDate_FullDateBetween(
                        Long productId,
                        Long locationId,
                        LocalDate startDate,
                        LocalDate endDate);

    @Query("SELECT COALESCE(SUM(f.quantity), 0), COALESCE(SUM(f.totalAmount), 0), COALESCE(SUM(f.transactionCount), 0) " +
            "FROM FactSales f " +
            "WHERE f.dimProduct.sourceProductId = :productId " +
            "AND f.dimLocation.sourceLocationId = :locationId " +
            "AND f.dimDate.fullDate BETWEEN :startDate AND :endDate")
    Object[] aggregateByProductLocationAndDateRange(@Param("productId") Long productId,
                                                     @Param("locationId") Long locationId,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);
}
