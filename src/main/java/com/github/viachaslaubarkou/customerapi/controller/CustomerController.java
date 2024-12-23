package com.github.viachaslaubarkou.customerapi.controller;

import com.github.viachaslaubarkou.customerapi.model.Customer;
import com.github.viachaslaubarkou.customerapi.service.CustomerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getAll")
    public List<Customer> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Customer> getById(@PathVariable("id") UUID id) {
        return customerService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Customer create(@RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @PostMapping("/update")
    public ResponseEntity<Customer> update(@RequestBody Customer updatedCustomer) {
        return ResponseEntity.ok(customerService.update(updatedCustomer));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") UUID id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
