package com.oltp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Location Entity - OLTP Optimized
 * Attributes suitable for transactional processing:
 * - Store/warehouse locations for sales tracking
 * - Geographic indexing for location-based queries
 * - Operating hours for business logic
 * - Store capacity and type information
 */
@Entity
@Table(name = "locations", indexes = {
    @Index(name = "idx_store_code", columnList = "store_code"),
    @Index(name = "idx_city", columnList = "city"),
    @Index(name = "idx_location_type", columnList = "location_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "store_code", nullable = false, unique = true, length = 20)
    private String storeCode;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(name = "location_type", nullable = false, length = 30)
    private String locationType; // RETAIL, WAREHOUSE, OUTLET, ONLINE

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "manager_name", length = 100)
    private String managerName;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @Column(name = "store_capacity")
    private Integer storeCapacity; // Square footage or customer capacity

    @Column(name = "location_status", nullable = false, length = 20)
    private String locationStatus; // ACTIVE, INACTIVE, UNDER_RENOVATION

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
