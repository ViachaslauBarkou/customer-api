package com.github.viachaslaubarkou.customerapi.service;

import com.github.viachaslaubarkou.customerapi.model.Customer;
import com.github.viachaslaubarkou.customerapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getById(UUID id) {
        return customerRepository.findById(id);
    }

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Customer updatedCustomer) {
        Customer customer = customerRepository.findById(updatedCustomer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setFirstName(updatedCustomer.getFirstName());
        customer.setMiddleName(updatedCustomer.getMiddleName());
        customer.setLastName(updatedCustomer.getLastName());
        customer.setEmailAddress(updatedCustomer.getEmailAddress());
        customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        return customerRepository.save(customer);
    }

    public void delete(UUID id) {
        customerRepository.deleteById(id);
    }
}
