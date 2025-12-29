package com.juaracoding.pcmspringboot29.controller;

import com.juaracoding.pcmspringboot29.dto.CustomerDTO;
import com.juaracoding.pcmspringboot29.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody CustomerDTO customerDTO,
                                       HttpServletRequest request){
        return customerService.save(customerService.mapMPToEntity(customerDTO),
                request);
    }
}
