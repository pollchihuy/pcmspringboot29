package com.juaracoding.pcmspringboot29.dto.respon;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.juaracoding.pcmspringboot29.dto.relasi.RelKategoriProdukDTO;
import com.juaracoding.pcmspringboot29.dto.relasi.RelSupplierDTO;
import com.juaracoding.pcmspringboot29.util.ConstantMessagez;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class RespProdukDTO {

    private Long id;
    private String nama;
    private String namaKategoriProduk;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamaKategoriProduk() {
        return namaKategoriProduk;
    }

    public void setNamaKategoriProduk(String namaKategoriProduk) {
        this.namaKategoriProduk = namaKategoriProduk;
    }
}