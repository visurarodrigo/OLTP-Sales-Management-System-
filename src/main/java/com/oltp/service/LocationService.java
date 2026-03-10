package com.oltp.service;

import com.oltp.entity.Location;
import com.oltp.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationByStoreCode(String storeCode) {
        return locationRepository.findByStoreCode(storeCode);
    }

    public List<Location> getLocationsByType(String type) {
        return locationRepository.findByLocationType(type);
    }

    public List<Location> getActiveLocations() {
        return locationRepository.findByLocationStatus("ACTIVE");
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
