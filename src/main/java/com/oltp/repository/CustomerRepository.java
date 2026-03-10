package com.oltp.repository;

import com.oltp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    List<Customer> findByCustomerStatus(String status);
    
    List<Customer> findByCity(String city);
    
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);
}
