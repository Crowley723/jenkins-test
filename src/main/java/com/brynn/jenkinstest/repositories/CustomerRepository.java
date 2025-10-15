package com.brynn.jenkinstest.repositories;

import com.brynn.jenkinstest.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByFirstName(String firstName);
    List<Customer> findByLastName(String lastName);
    List<Customer> findByid(Long id);
    List<Customer> findAll();
}