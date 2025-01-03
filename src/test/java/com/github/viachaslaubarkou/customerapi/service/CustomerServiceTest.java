package com.github.viachaslaubarkou.customerapi.service;

import com.github.viachaslaubarkou.customerapi.model.Customer;
import com.github.viachaslaubarkou.customerapi.repository.CustomerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Timer timer;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(meterRegistry.timer("customerapi.service.get.by.id.time")).thenReturn(timer);
        when(meterRegistry.timer("customerapi.service.get.by.name.time")).thenReturn(timer);
        when(meterRegistry.timer("customerapi.service.get.by.phone.number.time")).thenReturn(timer);
        when(timer.record((Supplier<Object>) any())).thenAnswer(invocation -> ((Supplier<?>) invocation.getArgument(0)).get());
        customerService = new CustomerService(customerRepository, meterRegistry);

        customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("Ivan");
        customer.setMiddleName("Ivanovich");
        customer.setLastName("Ivanov");
        customer.setEmailAddress("ivan.ivanov@example.com");
        customer.setPhoneNumber("1234567890");
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
    void getByPhoneNumber_ShouldReturnMatchingCustomers() {
        String searchPhone = "123";
        List<Customer> expectedCustomers = List.of(customer);

        when(customerRepository.findByPhoneNumberContaining(searchPhone)).thenReturn(expectedCustomers);

        List<Customer> result = customerService.getByPhoneNumber(searchPhone);

        assertEquals(1, result.size());
        assertTrue(result.contains(customer));
        verify(customerRepository, times(1)).findByPhoneNumberContaining(searchPhone);
    }

    @Test
    void getByPhoneNumber_ShouldThrowException_ForInvalidPhoneNumber() {
        String invalidPhone = "123-abc";

        assertThrows(IllegalArgumentException.class, () -> customerService.getByPhoneNumber(invalidPhone));

        verifyNoInteractions(customerRepository);
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
        updatedCustomer.setPhoneNumber("9876543210");

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
        updatedCustomer.setPhoneNumber("+9876543210");

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
