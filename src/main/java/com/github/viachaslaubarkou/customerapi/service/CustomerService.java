package com.github.viachaslaubarkou.customerapi.service;

import com.github.viachaslaubarkou.customerapi.model.Customer;
import com.github.viachaslaubarkou.customerapi.repository.CustomerRepository;
import com.github.viachaslaubarkou.customerapi.util.ValidationUtils;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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

    private final Timer getByIdTimer;
    private final Timer getByNameTimer;
    private final Timer getByPhoneNumberTimer;

    public CustomerService(CustomerRepository customerRepository, MeterRegistry meterRegistry) {
        this.customerRepository = customerRepository;
        getByIdTimer = meterRegistry.timer("customerapi.service.get.by.id.time");
        getByNameTimer = meterRegistry.timer("customerapi.service.get.by.name.time");
        getByPhoneNumberTimer = meterRegistry.timer("customerapi.service.get.by.phone.number.time");
    }

    public List<Customer> getAll() {
        logger.info("Getting all customers");
        return customerRepository.findAll();
    }

    public Optional<Customer> getById(UUID id) {
        logger.info("Getting customer by id: {}", id);
        return getByIdTimer.record(() -> {
            return customerRepository.findById(id);
        });
    }

    public List<Customer> getByName(String name) {
        logger.info("Getting customer by name: {}", name);
        return getByNameTimer.record(() -> {
            return customerRepository.findByNameContainingIgnoreCase(name);
        });
    }

    public List<Customer> getByPhoneNumber(String phoneNumber) {
        logger.info("Getting customer by phoneNumber: {}", phoneNumber);
        return getByPhoneNumberTimer.record(() -> {
            validatePhoneNumber(phoneNumber);
            return customerRepository.findByPhoneNumberContaining(phoneNumber);
        });
    }

    public Customer create(Customer customer) {
        logger.info("Creating new customer: {}", customer);
        validateCustomer(customer);

        return customerRepository.save(customer);
    }

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

    public void delete(UUID id) {
        customerRepository.deleteById(id);
    }

    private void validateCustomer(Customer customer) {
        validateEmail(customer.getEmailAddress());
        validatePhoneNumber(customer.getPhoneNumber());
    }

    private void validateEmail(String email) {
        if (!ValidationUtils.isValidEmail(email)) {
            logger.error("Wrong email format: {}", email);
            throw new IllegalArgumentException("Wrong email format");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!ValidationUtils.isValidPhoneNumber(phoneNumber)) {
            logger.error("Wrong phone number format: {}", phoneNumber);
            throw new IllegalArgumentException("Wrong phone number format");
        }
    }
}
