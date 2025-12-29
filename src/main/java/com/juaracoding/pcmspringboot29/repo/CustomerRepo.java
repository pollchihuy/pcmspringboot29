package com.juaracoding.pcmspringboot29.repo;

import com.juaracoding.pcmspringboot29.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
}