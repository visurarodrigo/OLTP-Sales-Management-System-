package com.oltp.repository;

import com.oltp.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    
    Optional<Sales> findByOrderNumber(String orderNumber);
    
    List<Sales> findByCustomer_CustomerId(Long customerId);
    
    List<Sales> findByProduct_ProductId(Long productId);
    
    List<Sales> findByLocation_LocationId(Long locationId);
    
    List<Sales> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Sales> findByOrderStatus(String orderStatus);
    
    List<Sales> findByPaymentStatus(String paymentStatus);
    
    @Query("SELECT SUM(s.totalAmount) FROM Sales s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalRevenue(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Sales s WHERE s.customer.customerId = :customerId AND s.orderStatus = 'COMPLETED'")
    List<Sales> findCompletedSalesByCustomer(@Param("customerId") Long customerId);
}
