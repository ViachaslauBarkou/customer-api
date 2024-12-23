package com.github.viachaslaubarkou.customerapi.repository;

import com.github.viachaslaubarkou.customerapi.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        customer1 = new Customer();
        customer1.setFirstName("Ivan");
        customer1.setMiddleName("Ivanovich");
        customer1.setLastName("Ivanov");
        customer1.setEmailAddress("ivan.ivanov@example.com");
        customer1.setPhoneNumber("123-456-7890");

        customer2 = new Customer();
        customer2.setFirstName("Petr");
        customer2.setMiddleName("Petrovich");
        customer2.setLastName("Petrov");
        customer2.setEmailAddress("petr.petrov@example.com");
        customer2.setPhoneNumber("987-654-3210");

        customer1 = customerRepository.save(customer1);
        customer2 = customerRepository.save(customer2);
    }

    @Test
    public void findById_ShouldReturnCustomer_WhenCustomerExists() {
        Optional<Customer> result = customerRepository.findById(customer1.getId());
        assertTrue(result.isPresent());
        assertEquals(customer1.getFirstName(), result.get().getFirstName());
    }

    @Test
    public void findById_ShouldReturnEmpty_WhenCustomerDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();
        Optional<Customer> result = customerRepository.findById(nonExistentId);
        assertFalse(result.isPresent());
    }

    @Test
    public void findAll_ShouldReturnListOfCustomers() {
        List<Customer> customers = customerRepository.findAll();
        assertEquals(2, customers.size());
    }

    @Test
    public void save_ShouldPersistCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Alex");
        newCustomer.setMiddleName("Alexandrovich");
        newCustomer.setLastName("Alexeev");
        newCustomer.setEmailAddress("alex.alexeev@example.com");
        newCustomer.setPhoneNumber("456-123-7890");

        Customer savedCustomer = customerRepository.save(newCustomer);
        assertNotNull(savedCustomer.getId());
        assertEquals("Alex", savedCustomer.getFirstName());
    }

    @Test
    public void delete_ShouldRemoveCustomer() {
        customerRepository.delete(customer1);

        Optional<Customer> result = customerRepository.findById(customer1.getId());
        assertFalse(result.isPresent());
    }
}
