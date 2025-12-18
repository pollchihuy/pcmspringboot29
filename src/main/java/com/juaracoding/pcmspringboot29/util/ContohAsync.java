package com.juaracoding.pcmspringboot29.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/*
IntelliJ IDEA 2024.1.4 (Ultimate Edition)
Build #IU-241.18034.62, built on June 21, 2024
@Author pollc a.k.a. Paul Christian
Java Developer
Created on 17/12/2025 19:35
@Last Modified 17/12/2025 19:35
Version 1.0
*/
public class ContohAsync {


    @Async
    public void printOut(){
        System.out.println("Print !!");
    }

    @Scheduled(cron = "* * 12 * *")
    public void jalankanJam12Malam(){
        System.out.println("Print !!");
    }
}
