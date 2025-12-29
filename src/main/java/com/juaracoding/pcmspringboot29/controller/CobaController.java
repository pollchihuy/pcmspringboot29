package com.juaracoding.pcmspringboot29.controller;

import com.juaracoding.pcmspringboot29.util.GlobalFunction;
import com.juaracoding.pcmspringboot29.util.LoggingFile;
import com.juaracoding.pcmspringboot29.util.RequestCapture;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Random;

@Controller
@RequestMapping("coba")
public class CobaController {

    @Autowired
    Random random ;


    // localhost:8080/coba
    @GetMapping
    public String welcomePage(HttpServletRequest request) throws IOException {

        System.out.println("Isi Content : ");
        GlobalFunction.printConsole("print deh disini !!");

        return "welcome";
    }

    // localhost:8080/coba/data
    @GetMapping("/data")
    public String data(){
        return "welcome";
    }

    // localhost:8080/coba/doang
    @GetMapping("/doang")
    public String doang(){
        return "welcome";
    }
}
