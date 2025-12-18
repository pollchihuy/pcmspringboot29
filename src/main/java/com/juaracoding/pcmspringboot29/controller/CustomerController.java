package com.juaracoding.pcmspringboot29.controller;


import com.juaracoding.pcmspringboot29.model.Customer;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("customer")
public class CustomerController {


    // GET localhost:8080/customer
    @GetMapping
    public Object getCustomer(){
        Map<String,Object> map = new HashMap<>();
        map.put("email","paul@gmail.com");
        map.put("nama","Paul");
        map.put("username","paul123");

        Customer customer = new Customer();
        customer.setEmail("paul@gmail.com");
        customer.setNama("Paul");
        customer.setUsername("paul123");
        return map;
    }

    //POST localhost:8080/customer
    @PostMapping
    public Object save(@RequestBody Customer customer){
//        Map<String,Object> map = new HashMap<>();
//        map.put("email","paul@gmail.com");
//        map.put("nama","Paul");
//        map.put("username","paul123");

        System.out.println("Nama : "+customer.getNama());
        System.out.println("Email : "+customer.getEmail());
        System.out.println("Username : "+customer.getUsername());
        System.out.println("Tanggal Lahir : "+customer.getTanggalLahir());
        return customer;
    }

    //PUT localhost:8080/customer/{id}/{token} - path Variabel
    @PutMapping("/{id_user}/{token}")
    public Object update(
            @PathVariable(value = "id_user") Long id,
            @PathVariable String token,
                         @RequestBody Customer customer){

        System.out.println("ID : "+id);
        System.out.println("TOKEN : "+token);
        System.out.println("Nama : "+customer.getNama());
        System.out.println("Email : "+customer.getEmail());
        System.out.println("Username : "+customer.getUsername());
        System.out.println("Tanggal Lahir : "+customer.getTanggalLahir());
        return id;
    }

    //Query Param / Request Param
    //GET localhost:8080/customer/{id}/{token}?column_name='nama'&value='Pau'&size=7
//    GET localhost:8080/customer/1/ini token bray?column_name=nama&value=Pau&size=7
    @GetMapping("/{id_user}/{token}")
    public Object getDataByParam(
            @PathVariable(value = "id_user") Long id,
            @PathVariable String token,
            @RequestParam(value = "column_name") String column,
            @RequestParam String value,
            @RequestParam Integer size){

        System.out.println("ID : "+id);
        System.out.println("TOKEN : "+token);
        System.out.println("COLUMN_NAME : "+column);
        System.out.println("VALUE : "+value);
        System.out.println("SIZE : "+size);
        return id;
    }
}
