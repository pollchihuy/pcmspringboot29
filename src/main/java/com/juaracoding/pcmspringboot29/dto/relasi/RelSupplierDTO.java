package com.juaracoding.pcmspringboot29.dto.relasi;

import jakarta.validation.constraints.NotNull;

/*
IntelliJ IDEA 2024.1.4 (Ultimate Edition)
Build #IU-241.18034.62, built on June 21, 2024
@Author pollc a.k.a. Paul Christian
Java Developer
Created on 06/01/2026 19:12
@Last Modified 06/01/2026 19:12
Version 1.0
*/
public class RelSupplierDTO {

    @NotNull
    private Long id;


    public @NotNull Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }
}
