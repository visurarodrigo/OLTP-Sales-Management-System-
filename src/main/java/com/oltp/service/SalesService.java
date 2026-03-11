package com.oltp.service;

import com.oltp.entity.Customer;
import com.oltp.entity.Location;
import com.oltp.entity.Product;
import com.oltp.entity.Sales;
import com.oltp.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesService {

    private final SalesRepository salesRepository;
    private final ProductService productService;

    @Transactional
    public Sales createSale(Sales sale) {
        // Calculate amounts
        BigDecimal subtotal = sale.getUnitPrice().multiply(new BigDecimal(sale.getQuantity()));
        sale.setSubtotal(subtotal);
        
        BigDecimal discountAmount = sale.getDiscountAmount() != null ? sale.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal taxableAmount = subtotal.subtract(discountAmount);
        BigDecimal taxAmount = taxableAmount.multiply(new BigDecimal("0.10")); // 10% tax
        sale.setTaxAmount(taxAmount);
        
        BigDecimal totalAmount = taxableAmount.add(taxAmount);
        sale.setTotalAmount(totalAmount);
        
        // Update product stock
        productService.updateStock(sale.getProduct().getProductId(), sale.getQuantity());
        
        return salesRepository.save(sale);
    }

    public Optional<Sales> getSaleById(Long id) {
        return salesRepository.findById(id);
    }

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    public List<Sales> getSalesByCustomer(Long customerId) {
        return salesRepository.findByCustomer_CustomerId(customerId);
    }

    public List<Sales> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return salesRepository.findBySaleDateBetween(startDate, endDate);
    }

    public BigDecimal calculateRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return salesRepository.calculateTotalRevenue(startDate, endDate);
    }

    public void deleteSale(Long id) {
        salesRepository.deleteById(id);
    }
}
