package com.oltp.repository;

import com.oltp.entity.SalesDatamartDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalesDatamartDailyRepository extends JpaRepository<SalesDatamartDaily, Long> {

    List<SalesDatamartDaily> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);
}
