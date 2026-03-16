package com.oltp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "dim_location", indexes = {
        @Index(name = "idx_dim_location_source_id", columnList = "source_location_id"),
        @Index(name = "idx_dim_location_city_state", columnList = "city, state")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dim_location_id")
    private Long dimLocationId;

    @Column(name = "source_location_id", nullable = false, unique = true)
    private Long sourceLocationId;

    @Column(name = "store_code", nullable = false, length = 20)
    private String storeCode;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(name = "location_type", nullable = false, length = 30)
    private String locationType;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @Column(name = "country", nullable = false, length = 50)
    private String country;
}
