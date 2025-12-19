package com.juaracoding.pcmspringboot29.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

public class CustomerDTO {
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$",message = "Nama lengkap hanya boleh mengandung huruf, spasi, dan karakter tanda baca standar (seperti tanda hubung, titik, atau tanda petik tunggal). Pastikan nama tidak dimulai dengan karakter spesial atau angka.")
    private String nama;
    @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,})+$",message = "wajib format email")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,16}$",message = "nama alfanumerik min 3 max 16")
    private String username;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("tanggal_lahir")
    private String tanggalLahir;

    public @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Nama lengkap hanya boleh mengandung huruf, spasi, dan karakter tanda baca standar (seperti tanda hubung, titik, atau tanda petik tunggal). Pastikan nama tidak dimulai dengan karakter spesial atau angka.") String getNama() {
        return nama;
    }

    public void setNama(@Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Nama lengkap hanya boleh mengandung huruf, spasi, dan karakter tanda baca standar (seperti tanda hubung, titik, atau tanda petik tunggal). Pastikan nama tidak dimulai dengan karakter spesial atau angka.") String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^[a-zA-Z0-9._-]{3,16}$", message = "nama alfanumerik min 3 max 16") String getUsername() {
        return username;
    }

    public void setUsername(@Pattern(regexp = "^[a-zA-Z0-9._-]{3,16}$", message = "nama alfanumerik min 3 max 16") String username) {
        this.username = username;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}
