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

import static org.hamcrest.Matchers.hasSize;
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

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        customer1 = new Customer();
        customer1.setId(UUID.randomUUID());
        customer1.setFirstName("Ivan");
        customer1.setMiddleName("Ivanovich");
        customer1.setLastName("Ivanov");
        customer1.setEmailAddress("ivan.ivanov@example.com");
        customer1.setPhoneNumber("+1234567890");

        customer2 = new Customer();
        customer2.setId(UUID.randomUUID());
        customer2.setFirstName("Anna");
        customer2.setMiddleName("Ivanovna");
        customer2.setLastName("Sidorova");
        customer2.setEmailAddress("anna.sidorova@example.com");
        customer2.setPhoneNumber("+987654321");
    }

    @Test
    public void getAll_ShouldReturnListOfCustomers() throws Exception {
        List<Customer> customers = List.of(customer1);
        when(customerService.getAll()).thenReturn(customers);

        mockMvc.perform(get("/api/customers/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(customers.size()))
                .andExpect(jsonPath("$[0].firstName").value("Ivan"));

        verify(customerService, times(1)).getAll();
    }

    @Test
    public void getById_ShouldReturnCustomer_WhenCustomerExists() throws Exception {
        when(customerService.getById(customer1.getId())).thenReturn(Optional.of(customer1));

        mockMvc.perform(get("/api/customers/getById")
                        .param("id", customer1.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.emailAddress").value("ivan.ivanov@example.com"));

        verify(customerService, times(1)).getById(customer1.getId());
    }

    @Test
    public void getById_ShouldReturnNotFound_WhenCustomerDoesNotExist() throws Exception {
        when(customerService.getById(customer1.getId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/getById")
                        .param("id", customer1.getId().toString()))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).getById(customer1.getId());
    }

    @Test
    void getByName_ShouldReturnMatchingCustomers() throws Exception {
        String searchName = "ivan";
        List<Customer> expectedCustomers = List.of(customer1, customer2);

        when(customerService.getByName(searchName)).thenReturn(expectedCustomers);

        mockMvc.perform(get("/api/customers/getByName")
                        .param("name", searchName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Ivan"))
                .andExpect(jsonPath("$[1].firstName").value("Anna"));

        verify(customerService, times(1)).getByName(searchName);
    }

    @Test
    void getByPhoneNumber_ShouldReturnMatchingCustomers() throws Exception {
        String searchPhone = "123";
        List<Customer> expectedCustomers = List.of(customer1);

        when(customerService.getByPhoneNumber(searchPhone)).thenReturn(expectedCustomers);

        mockMvc.perform(get("/api/customers/getByPhoneNumber")
                        .param("phoneNumber", searchPhone))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].phoneNumber").value("+1234567890"));

        verify(customerService, times(1)).getByPhoneNumber(searchPhone);
    }


    @Test
    public void create_ShouldReturnCreatedCustomer() throws Exception {
        when(customerService.create(any(Customer.class))).thenReturn(customer1);

        mockMvc.perform(post("/api/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.emailAddress").value("ivan.ivanov@example.com"));

        verify(customerService, times(1)).create(any(Customer.class));
    }

    @Test
    public void update_ShouldReturnUpdatedCustomer() throws Exception {
        when(customerService.update(any(Customer.class))).thenReturn(customer1);

        mockMvc.perform(post("/api/customers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.emailAddress").value("ivan.ivanov@example.com"));

        verify(customerService, times(1)).update(any(Customer.class));
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer() throws Exception {
        UUID id = customer1.getId();

        doNothing().when(customerService).delete(id);

        mockMvc.perform(delete("/api/customers/delete")
                        .param("id", id.toString()))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).delete(id);
    }
}
