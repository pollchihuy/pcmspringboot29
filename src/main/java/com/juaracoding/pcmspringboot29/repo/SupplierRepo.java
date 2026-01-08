package com.juaracoding.pcmspringboot29.repo;

import com.juaracoding.pcmspringboot29.model.KategoriProduk;
import com.juaracoding.pcmspringboot29.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier,Long> {
    Page<Supplier> findByNamaContains(Pageable page , String value);
    List<Supplier> findByNamaContains(String value);
}
