package com.oltp.repository;

import com.oltp.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    Optional<Location> findByStoreCode(String storeCode);
    
    List<Location> findByLocationType(String locationType);
    
    List<Location> findByCity(String city);
    
    List<Location> findByLocationStatus(String status);
    
    List<Location> findByStateAndCountry(String state, String country);
}
