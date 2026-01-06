package com.juaracoding.pcmspringboot29.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MstProduk")
public class Produk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NamaProduk",nullable = false,unique = true,length = 40)
    private String nama;

    /** Many to One
     * Change When Migration
     * */
    @ManyToOne
    @JoinColumn(name = "IDKategoriProduk",foreignKey = @ForeignKey(name = "fk-to-katprod",foreignKeyDefinition = ""),nullable = false)
    private KategoriProduk kategoriProduk;

    @ManyToMany
    @JoinTable(name = "ProdukSupplier",
    uniqueConstraints = @UniqueConstraint(name = "unq-produk-supplier",columnNames = {"ProdukID","SupplierID"}),
    joinColumns = @JoinColumn(name = "ProdukID",foreignKey = @ForeignKey(name = "fk-mtm-produk")),
    inverseJoinColumns = @JoinColumn(name = "SupplierID",foreignKey = @ForeignKey(name = "fk-mtm-supplier"))
    )
    private List<Supplier> supplier;

    @Column(name = "CreatedBy",updatable = false,nullable = false)
    private Long createdBy=1L;

    @Column(name = "CreatedDate",updatable = false,nullable = false)
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "ModifiedBy",insertable = false)
    private Long modifiedBy;

    @Column(name = "ModifiedDate",insertable = false)
    @UpdateTimestamp
    private Date modifiedDate;

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

    public KategoriProduk getKategoriProduk() {
        return kategoriProduk;
    }

    public void setKategoriProduk(KategoriProduk kategoriProduk) {
        this.kategoriProduk = kategoriProduk;
    }

    public List<Supplier> getSupplier() {
        return supplier;
    }

    public void setSupplier(List<Supplier> supplier) {
        this.supplier = supplier;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
