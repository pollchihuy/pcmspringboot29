package com.juaracoding.pcmspringboot29.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

@Controller
@RequestMapping("coba")
public class CobaController {

    @Autowired
    Random random ;



    @GetMapping
    public String welcomePage(){
        return "welcome";
    }
}
