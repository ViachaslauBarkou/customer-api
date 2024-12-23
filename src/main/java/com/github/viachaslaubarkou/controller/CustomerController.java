package com.github.viachaslaubarkou.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @GetMapping("/get-all")
    public String getAll() {
        return "Stub to get all customers";
    }
}
