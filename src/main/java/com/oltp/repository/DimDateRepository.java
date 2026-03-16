package com.oltp.repository;

import com.oltp.entity.DimDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DimDateRepository extends JpaRepository<DimDate, Long> {

    Optional<DimDate> findByFullDate(LocalDate fullDate);
}
