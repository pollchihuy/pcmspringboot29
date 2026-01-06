package com.juaracoding.pcmspringboot29.contoh;

import com.juaracoding.pcmspringboot29.security.Crypto;

/*
IntelliJ IDEA 2024.1.4 (Ultimate Edition)
Build #IU-241.18034.62, built on June 21, 2024
@Author pollc a.k.a. Paul Christian
Java Developer
Created on 06/01/2026 20:53
@Last Modified 06/01/2026 20:53
Version 1.0
*/
public class ContohOneTimeToken {


    public static void main(String[] args) {
        //stateless
        //C405#14_paul321@gmail.com#1767707704854
        //b4266b9f8f31fd06b671d22f0b697ac293cb2bc5d002192fcea124e847d8df3a87fea98ffd3c73d33e84d3c490747e7b
        System.out.println(Crypto.performEncrypt("C405#14_paul321@gmail.com#1767707704854"));
        System.out.println(System.currentTimeMillis());
        String strDecrypt = Crypto.performDecrypt("b4266b9f8f31fd06b671d22f0b697ac293cb2bc5d002192fcea124e847d8df3a87fea98ffd3c73d33e84d3c490747e7b");
        System.out.println("Decrypt : "+strDecrypt);
        String [] strArr = strDecrypt.split("#");
        for (String s:
             strArr) {
            System.out.println(s);
        }
        //masukin email -> response token estafe 272712
        //otp -> waktu submit membawa sebuah data / info dari proses sebeleumnya -> 551344
        //confirm password baru -> waktu submit membawa sebuah data / info dari proses sebeleumnya -> update 774248
        // tiban otp
    }
}