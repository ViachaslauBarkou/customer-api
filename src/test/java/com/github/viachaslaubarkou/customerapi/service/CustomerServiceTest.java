package com.github.viachaslaubarkou.customerapi.service;

import com.github.viachaslaubarkou.customerapi.model.Customer;
import com.github.viachaslaubarkou.customerapi.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("Ivan");
        customer.setMiddleName("Ivanovich");
        customer.setLastName("Ivanov");
        customer.setEmailAddress("ivan.ivanov@example.com");
        customer.setPhoneNumber("123-456-7890");
    }

    @Test
    public void getAll_ShouldReturnListOfCustomers() {
        List<Customer> customers = List.of(customer);
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAll();

        assertEquals(customers.size(), result.size());
        assertEquals(customer.getFirstName(), result.get(0).getFirstName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void getById_ShouldReturnCustomer_WhenCustomerExists() {
        UUID id = customer.getId();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(customer.getFirstName(), result.get().getFirstName());
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    public void getById_ShouldReturnEmptyOptional_WhenCustomerDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.getById(id);

        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    public void create_ShouldSaveAndReturnCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.create(customer);

        assertNotNull(result);
        assertEquals(customer.getFirstName(), result.getFirstName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void update_ShouldUpdateAndReturnCustomer_WhenCustomerExists() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(customer.getId());
        updatedCustomer.setFirstName("Updated");
        updatedCustomer.setMiddleName("Updatedovich");
        updatedCustomer.setLastName("Updatedov");
        updatedCustomer.setEmailAddress("updated.email@example.com");
        updatedCustomer.setPhoneNumber("987-654-3210");

        Customer result = customerService.update(updatedCustomer);

        assertNotNull(result);
        assertEquals("Updated", result.getFirstName());
        verify(customerRepository, times(1)).findById(customer.getId());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void update_ShouldThrowException_WhenCustomerDoesNotExist() {
        UUID id = UUID.randomUUID();
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setFirstName("Updated");
        updatedCustomer.setMiddleName("Updatedovich");
        updatedCustomer.setLastName("Updatedov");
        updatedCustomer.setEmailAddress("updated.email@example.com");
        updatedCustomer.setPhoneNumber("987-654-3210");

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> customerService.update(updatedCustomer));
        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void delete_ShouldDeleteCustomer() {
        UUID id = customer.getId();
        doNothing().when(customerRepository).deleteById(id);

        customerService.delete(id);

        verify(customerRepository, times(1)).deleteById(id);
    }
}
