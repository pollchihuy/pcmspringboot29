package com.juaracoding.pcmspringboot29.repo;

import com.juaracoding.pcmspringboot29.model.KategoriProduk;
import com.juaracoding.pcmspringboot29.model.LogKategoriProduk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LogKategoriProdukRepo extends CrudRepository<LogKategoriProduk,Long> {
}