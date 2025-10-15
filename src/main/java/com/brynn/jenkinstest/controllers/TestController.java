package com.brynn.jenkinstest.controllers;

import com.brynn.jenkinstest.model.Customer;
import com.brynn.jenkinstest.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TestController {
    private final CustomerRepository customerRepository;

    public TestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("/customer")
    public ResponseEntity CreateNewCustomer(@RequestBody Customer customer){
        if (customer.getFirstName() == null || customer.getLastName() == null){
            return ResponseEntity.badRequest().build();
        }
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(savedCustomer);
    }

    @GetMapping("/customer")
    public List<Customer> GetAllCustomers(){
        return customerRepository.findAll();
    }

    @GetMapping("/customer/{name}")
    public ResponseEntity<Object> GetCustomer(@PathVariable("name") String name) {
        List<Customer> customers = customerRepository.findByFirstName(name);

        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        return ResponseEntity.ok(customers);
    }


    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("version", "1.0.0");
        return status;
    }
}
