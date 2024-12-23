package com.github.viachaslaubarkou.customerapi.service;

import com.github.viachaslaubarkou.customerapi.model.Customer;
import com.github.viachaslaubarkou.customerapi.repository.CustomerRepository;
import com.github.viachaslaubarkou.customerapi.util.ValidationUtils;
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

    public List<Customer> getByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Customer> getByPhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        return customerRepository.findByPhoneNumberContaining(phoneNumber);
    }

    public Customer create(Customer customer) {
        validateCustomer(customer);

        return customerRepository.save(customer);
    }

    public Customer update(Customer updatedCustomer) {
        validateCustomer(updatedCustomer);

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

    private void validateCustomer(Customer customer) {
        validateEmail(customer.getEmailAddress());
        validatePhoneNumber(customer.getPhoneNumber());
    }

    private void validateEmail(String email) {
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!ValidationUtils.isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}
