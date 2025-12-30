package com.juaracoding.pcmspringboot29.dto.respon;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.Period;

public class RespCustomerDTO {
    private Long id;
    private String nama;
    private String email;
    @JsonProperty("tanggal_lahir")
    private String tanggalLahir;
    @JsonProperty("no_hp")
    private String noHp;
    private String tambahan;
    private Integer umur;

    public Integer getUmur() {
        return Period.between(LocalDate.parse(tanggalLahir),LocalDate.now()).getYears();
    }

    public void setUmur(Integer umur) {
        this.umur = umur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTambahan() {
        return tambahan;
    }

    public void setTambahan(String tambahan) {
        this.tambahan = tambahan;
    }

    public @Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,13}$",
            message = "Format No HP Tidak Valid , min 9 max 13 setelah angka 8, contoh : (0/62/+62)81111111") String getNoHp() {
        return noHp;
    }

    public void setNoHp(@Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,13}$",
            message = "Format No HP Tidak Valid , min 9 max 13 setelah angka 8, contoh : (0/62/+62)81111111") String noHp) {
        this.noHp = noHp;
    }

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

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }
}
