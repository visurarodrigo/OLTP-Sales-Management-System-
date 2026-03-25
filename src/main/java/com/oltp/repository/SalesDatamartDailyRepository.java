package com.oltp.repository;

import com.oltp.entity.SalesDatamartDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalesDatamartDailyRepository extends JpaRepository<SalesDatamartDaily, Long> {

    List<SalesDatamartDaily> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    List<SalesDatamartDaily> findBySaleDateBetweenAndSourceProductId(LocalDate startDate, LocalDate endDate, Long sourceProductId);

    List<SalesDatamartDaily> findBySaleDateBetweenAndSourceLocationId(LocalDate startDate, LocalDate endDate, Long sourceLocationId);

    List<SalesDatamartDaily> findBySaleDateBetweenAndSourceProductIdAndSourceLocationId(LocalDate startDate,
                                                                                          LocalDate endDate,
                                                                                          Long sourceProductId,
                                                                                          Long sourceLocationId);
}
