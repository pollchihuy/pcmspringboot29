package com.juaracoding.pcmspringboot29.repo;

import com.juaracoding.pcmspringboot29.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
    /**
     * untuk view di UI Web
     * SELECT * FROM MstCustomer WHERE toLower(Nama) LIKE ('%',toLower(?),'%') offset ? fetch ? */
    Page<Customer> findByNamaContains(Pageable page , String value);
    Page<Customer> findByNoHpContains(Pageable page , String value);
    Page<Customer> findByEmailContains(Pageable page , String value);
    @Query(value = "SELECT c FROM Customer c WHERE cast(c.tanggalLahir as string) LIKE CONCAT('%',?1,'%') ")
    Page<Customer> cariTanggal(Pageable page , String value);
    @Query(value = "SELECT c FROM Customer c WHERE cast(datediff(year,c.tanggalLahir,current_timestamp) as string) LIKE CONCAT('%',?1,'%') ")
    Page<Customer> liatUmur(Pageable page , String value);

    /**
     * untuk report
     * SELECT * FROM MstCustomer WHERE toLower(Nama) LIKE ('%',toLower(?),'%') */
    List<Customer> findByNamaContains(String value);
    List<Customer> findByNoHpContains(String value);
    List<Customer> findByEmailContains(String value);
    @Query(value = "SELECT c FROM Customer c WHERE cast(c.tanggalLahir as string) LIKE CONCAT('%',?1,'%') ")
    List<Customer> cariTanggal(String value);

}