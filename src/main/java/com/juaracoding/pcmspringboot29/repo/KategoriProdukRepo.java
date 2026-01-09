package com.juaracoding.pcmspringboot29.repo;

import com.juaracoding.pcmspringboot29.model.Customer;
import com.juaracoding.pcmspringboot29.model.KategoriProduk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KategoriProdukRepo extends JpaRepository<KategoriProduk,Long> {
    Page<KategoriProduk> findByNamaContains(Pageable page , String value);
    List<KategoriProduk> findByNamaContains(String value);

    /** query bantuan hanya untuk unit testing
     * SELECT TOP 1 * FROM MstKategoriProduk ORDER BY DESC
     * */
    Optional<KategoriProduk> findTop1ByOrderByIdDesc();
}