package com.juaracoding.pcmspringboot29.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
IntelliJ IDEA 2024.1.4 (Ultimate Edition)
Build #IU-241.18034.62, built on June 21, 2024
@Author pollc a.k.a. Paul Christian
Java Developer
Created on 18/12/2025 21:10
@Last Modified 18/12/2025 21:10
Version 1.0
*/
public class Customer {

    private String nama;
    private String email;
    private String username;
    @JsonProperty("tanggal_lahir")
    private String tanggalLahir;

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
