package com.juaracoding.pcmspringboot29.repo;

import com.juaracoding.pcmspringboot29.model.KategoriProduk;
import com.juaracoding.pcmspringboot29.model.Produk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdukRepo extends JpaRepository<Produk,Long> {
    Page<KategoriProduk> findByNamaContains(Pageable page , String value);
    List<KategoriProduk> findByNamaContains(String value);
}