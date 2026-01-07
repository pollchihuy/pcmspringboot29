package com.juaracoding.pcmspringboot29.controller;

import com.juaracoding.pcmspringboot29.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cobarest")
public class CobaRestController {

    @Value("${cumi.goreng}")
    private String x;
    @GetMapping
    public Object welcomePage(){
        User user = new User();
//        user.setNama("Paul");
        user.setAlamat("Bogor");
        return user;
    }

    @GetMapping("/1")
    public Object testProp(){

        return x;
    }
}
