package com.juaracoding.pcmspringboot29.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "MstSupplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NamaSupplier",nullable = false,unique = true,length = 40)
    private String nama;

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
