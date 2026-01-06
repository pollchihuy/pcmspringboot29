package com.juaracoding.pcmspringboot29.dto.validasi;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.juaracoding.pcmspringboot29.dto.relasi.RelKategoriProdukDTO;
import com.juaracoding.pcmspringboot29.dto.relasi.RelSupplierDTO;
import com.juaracoding.pcmspringboot29.model.Supplier;
import com.juaracoding.pcmspringboot29.util.ConstantMessagez;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class ValProdukDTO {
    @NotNull(message = ConstantMessagez.VAL_NAMA_KAT_PROD)
    @Pattern(regexp = "^[\\w\\s]{10,40}$",message = "Format Nama alfanumerik spasi min 10 maks 40")
    private String nama;

    @JsonProperty("list_supplier")
    private List<RelSupplierDTO> supplier;

    @JsonProperty("kategori_produk")
    private RelKategoriProdukDTO kategoriProduk;

    public List<RelSupplierDTO> getSupplier() {
        return supplier;
    }

    public void setSupplier(List<RelSupplierDTO> supplier) {
        this.supplier = supplier;
    }

    public RelKategoriProdukDTO getKategoriProduk() {
        return kategoriProduk;
    }

    public void setKategoriProduk(RelKategoriProdukDTO kategoriProduk) {
        this.kategoriProduk = kategoriProduk;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
