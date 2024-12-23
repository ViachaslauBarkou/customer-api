package com.github.viachaslaubarkou.customerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.viachaslaubarkou.customerapi.model.Customer;
import com.github.viachaslaubarkou.customerapi.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("Ivan");
        customer.setMiddleName("Ivanovich");
        customer.setLastName("Ivanov");
        customer.setEmailAddress("ivan.ivanov@example.com");
        customer.setPhoneNumber("123-456-7890");
    }

    @Test
    public void getAll_ShouldReturnListOfCustomers() throws Exception {
        List<Customer> customers = List.of(customer);
        when(customerService.getAll()).thenReturn(customers);

        mockMvc.perform(get("/api/customers/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(customers.size()))
                .andExpect(jsonPath("$[0].firstName").value("Ivan"));

        verify(customerService, times(1)).getAll();
    }

    @Test
    public void getById_ShouldReturnCustomer_WhenCustomerExists() throws Exception {
        when(customerService.getById(customer.getId())).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/api/customers/getById/{id}", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.emailAddress").value("ivan.ivanov@example.com"));

        verify(customerService, times(1)).getById(customer.getId());
    }

    @Test
    public void getById_ShouldReturnNotFound_WhenCustomerDoesNotExist() throws Exception {
        when(customerService.getById(customer.getId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/getById/{id}", customer.getId()))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).getById(customer.getId());
    }

    @Test
    public void create_ShouldReturnCreatedCustomer() throws Exception {
        when(customerService.create(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.emailAddress").value("ivan.ivanov@example.com"));

        verify(customerService, times(1)).create(any(Customer.class));
    }

    @Test
    public void update_ShouldReturnUpdatedCustomer() throws Exception {
        when(customerService.update(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/customers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.emailAddress").value("ivan.ivanov@example.com"));

        verify(customerService, times(1)).update(any(Customer.class));
    }

    @Test
    public void deleteCustomer_ShouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(customerService).delete(id);

        mockMvc.perform(delete("/api/customers/delete/" + id))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).delete(id);
    }
}
