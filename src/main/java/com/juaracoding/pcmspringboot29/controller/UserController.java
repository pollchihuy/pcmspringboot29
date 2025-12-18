package com.juaracoding.pcmspringboot29.controller;


import com.juaracoding.pcmspringboot29.util.ClassTidakBerperan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    ClassTidakBerperan classTidakBerperan;

    @GetMapping("/coba")
    public String coba(){
        return classTidakBerperan.printOK();
    }


//    public void isNull(){
//        if(classTidakBerperan==null){
//            classTidakBerperan = new ClassTidakBerperan();
//        }
//    }
}
