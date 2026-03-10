package com.oltp.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Sales Entity - OLTP Optimized
 * Attributes suitable for transactional processing:
 * - Foreign key relationships for data integrity
 * - Indexed columns for fast queries by date, customer, product
 * - Transaction-level details (quantity, amount, tax)
 * - Payment and order status for workflow management
 * - Timestamps for audit and reporting
 */
@Entity
@Table(name = "sales", indexes = {
    @Index(name = "idx_sale_date", columnList = "sale_date"),
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_product_id", columnList = "product_id"),
    @Index(name = "idx_location_id", columnList = "location_id"),
    @Index(name = "idx_order_status", columnList = "order_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_customer"))
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sales_location"))
    private Location location;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod; // CASH, CREDIT_CARD, DEBIT_CARD, DIGITAL_WALLET

    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus; // PAID, PENDING, REFUNDED, FAILED

    @Column(name = "order_status", nullable = false, length = 20)
    private String orderStatus; // COMPLETED, PROCESSING, CANCELLED, RETURNED

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
