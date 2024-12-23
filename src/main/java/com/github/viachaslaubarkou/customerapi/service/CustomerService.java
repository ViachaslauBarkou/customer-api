package com.github.viachaslaubarkou.customerapi.service;

import com.github.viachaslaubarkou.customerapi.model.Customer;
import com.github.viachaslaubarkou.customerapi.repository.CustomerRepository;
import com.github.viachaslaubarkou.customerapi.util.ValidationUtils;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Timed(value = "customerapi.service.get.all.time")
    public List<Customer> getAll() {
        logger.info("Getting all customers");
        return customerRepository.findAll();
    }

    @Timed(value = "customerapi.service.get.by.id.time")
    public Optional<Customer> getById(UUID id) {
        logger.info("Getting customer by id: {}", id);
        return customerRepository.findById(id);
    }

    @Timed(value = "customerapi.service.get.by.name.time")
    public List<Customer> getByName(String name) {
        logger.info("Getting customer by name: {}", name);
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    @Timed(value = "customerapi.service.get.by.phone.number.time")
    public List<Customer> getByPhoneNumber(String phoneNumber) {
        logger.info("Getting customer by phoneNumber: {}", phoneNumber);
        validatePhoneNumber(phoneNumber);
        return customerRepository.findByPhoneNumberContaining(phoneNumber);
    }

    @Timed(value = "customerapi.service.create.time")
    public Customer create(Customer customer) {
        logger.info("Creating new customer: {}", customer);
        validateCustomer(customer);

        return customerRepository.save(customer);
    }

    @Timed(value = "customerapi.service.update.time")
    public Customer update(Customer updatedCustomer) {
        logger.info("Updating customer with id: {}", updatedCustomer.getId());
        validateCustomer(updatedCustomer);

        Customer customer = customerRepository.findById(updatedCustomer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        logger.info("Old customer data: {}, new customer data: {}", customer, updatedCustomer);
        customer.setFirstName(updatedCustomer.getFirstName());
        customer.setMiddleName(updatedCustomer.getMiddleName());
        customer.setLastName(updatedCustomer.getLastName());
        customer.setEmailAddress(updatedCustomer.getEmailAddress());
        customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        return customerRepository.save(customer);
    }

    @Timed(value = "customerapi.service.delete.time")
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
