package com.juaracoding.pcmspringboot29.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "MstCustomer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NamaLengkap" , length = 60,nullable = false)
    private String nama;
    @Column(name = "Email" , length = 64,nullable = false,unique = true)
    private String email;
    @Column(name = "Username" , length = 20,nullable = false,unique = true)
    private String username;
    @Column(name = "NoHp",length = 20,nullable = false,unique = true)
    private String noHp;
    @Column(name = "Password" , length = 64,nullable = false,unique = true)
    private String password;
    @Column(name = "TanggalLahir",unique = true)
    private LocalDate tanggalLahir;
    @Transient
    private Integer umur;
    private String tambahan;

    public String getTambahan() {
        return tambahan;
    }

    public void setTambahan(String tambahan) {
        this.tambahan = tambahan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUmur() {
        return Period.between(tanggalLahir,LocalDate.now()).getYears();
    }

    public void setUmur(Integer umur) {
        this.umur = umur;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
