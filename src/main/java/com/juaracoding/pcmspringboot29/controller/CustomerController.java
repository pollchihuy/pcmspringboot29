package com.juaracoding.pcmspringboot29.controller;

import com.juaracoding.pcmspringboot29.dto.CustomerDTO;
import com.juaracoding.pcmspringboot29.handler.ResponseHandler;
import com.juaracoding.pcmspringboot29.services.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // POST localhost:8080/customer
    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody CustomerDTO customerDTO,
                                       HttpServletRequest request){
        return customerService.save(customerService.mapMPToEntity(customerDTO),
                request);
    }

    // PUT localhost:8080/customer/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody CustomerDTO customerDTO,
                                       @PathVariable Long id,
                                       HttpServletRequest request){
        return customerService.update(id,customerService.mapMPToEntity(customerDTO),
                request);
    }

    // DELETE localhost:8080/customer/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id,
                                         HttpServletRequest request){
        return customerService.delete(id,request);
    }

    // GET localhost:8080/customer/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id,
                                         HttpServletRequest request){
        return customerService.findById(id,request);
    }

    // GET localhost:8080/customer
    @GetMapping
    public ResponseEntity<Object> findAll(HttpServletRequest request){
        Pageable pageable = null;
        /** Pagination dimana yang ditampilkan adalah 10 data halaman pertama
         *  diurutkan ascending berdasarkan kolom id
         */
        pageable = PageRequest.of(0,10, Sort.by("id"));// int int sort (asc)
        return customerService.findAll(pageable,request);
    }

    /**
     * SELECT * FROM MstCustomer
     * SELECT c FROM CUSTOMER c
     * SELECT c FROM CUSTOMER c WHERE name = ?1
     * findByName -> jpql (derived Query)
     * GET localhost:8080/customer/asc/id/0?column=nama&size=10&value=Paul Ch
     */
    @GetMapping("/{sort}/{sort-by}/{page}")
    public ResponseEntity<Object> findByParam(
            @PathVariable String sort,
            @PathVariable(value = "sort-by") String sortBy,
            @PathVariable Integer page,
            @RequestParam Integer size,
            @RequestParam String column,
            @RequestParam String value,
            HttpServletRequest request){
        Pageable pageable = null;
        /** untuk memvalidasi kolom yang akan di sorting */
        sortBy = sortColumn(sortBy);
        if(sortBy.equals("false")){
            return new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN", HttpStatus.BAD_REQUEST,null,
                    "MST10V051",request);
        }
        switch (sort) {
            case "desc":pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());break;
            default:pageable = PageRequest.of(page,size, Sort.by(sortBy));break;
        }

        return customerService.findByParam(pageable,column,value,request);
    }
    private String sortColumn(String column){
        switch (column){
            case "id":column="id";break;
            case "nama":column="nama";break;
            case "email":column="email";break;
            case "noHp":column="noHp";break;
            case "tanggalLahir":column="tanggalLahir";break;
            case "tambahan":column="tambahan";break;
            default:column="false";break;
        }
        return column;
    }
}