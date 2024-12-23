package com.github.viachaslaubarkou.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerControllerTest {

    @Autowired
    CustomerController customerController;

    @Test
    public void testStubGetAllCustomers() {
        String result = customerController.getAll();
        assertEquals(result, "Stub to get all customers");
    }
}