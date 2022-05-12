package com.marcode.ebanking.repositories;

import com.marcode.ebanking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
