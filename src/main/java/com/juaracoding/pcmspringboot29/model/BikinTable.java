package com.juaracoding.pcmspringboot29.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "MstBikinTable"
//uniqueConstraints = @UniqueConstraint(name = "unq-combi-nama-umur",columnNames = {"Nama","Umur"}),
//indexes = {@Index(name = "idx-nama", columnList = "Nama,Umur", unique = true)}
//indexes = {@Index(name = "idx-umur", columnList = "Umur", unique = true)}
//indexes = {@Index(name = "idx-nama", columnList = "Nama", unique = true)}
)
public class BikinTable {

    @Id
    @Column(name = "CodeApa")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nama" ,length = 40,unique = true)
    private String nama;

    /** Change When Migration */
    @Column(name = "Umur",columnDefinition = "TINYINT")
    private Byte umur;

    @Column(name = "Gaji",precision = 10,scale = 2)
    private BigDecimal gaji;

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


}