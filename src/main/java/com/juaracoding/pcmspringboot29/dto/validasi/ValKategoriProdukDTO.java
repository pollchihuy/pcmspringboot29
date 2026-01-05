package com.juaracoding.pcmspringboot29.dto.validasi;


import com.juaracoding.pcmspringboot29.util.ConstantMessagez;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

public class ValKategoriProdukDTO {
    @NotNull(message = ConstantMessagez.VAL_NAMA_KAT_PROD)
    @Pattern(regexp = "^[\\w\\s]{10,40}$",message = "Format Nama alfanumerik spasi min 10 maks 40")
    private String nama;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
