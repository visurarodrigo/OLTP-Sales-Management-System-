package com.oltp.repository;

import com.oltp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySku(String sku);
    
    List<Product> findByCategory(String category);
    
    List<Product> findByProductStatus(String status);
    
    List<Product> findByStockQuantityLessThan(Integer quantity);
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.reorderLevel")
    List<Product> findProductsNeedingReorder();
    
    List<Product> findByProductNameContainingIgnoreCase(String name);
}
