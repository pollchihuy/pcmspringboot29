package com.juaracoding.pcmspringboot29.controller;

import com.juaracoding.pcmspringboot29.dto.validasi.ValKategoriProdukDTO;
import com.juaracoding.pcmspringboot29.handler.ResponseHandler;
import com.juaracoding.pcmspringboot29.services.KategoriProdukService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("kategoriProduk")
public class KategoriProdukController {

    @Autowired
    private KategoriProdukService kategoriProdukService;

    // POST localhost:8080/kategoriProduk
    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ValKategoriProdukDTO valKategoriProdukDTO,
                                       HttpServletRequest request){
        return kategoriProdukService.save(kategoriProdukService.mapMPToEntity(valKategoriProdukDTO),
                request);
    }

    // PUT localhost:8080/kategoriProduk/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody ValKategoriProdukDTO valKategoriProdukDTO,
                                       @PathVariable Long id,
                                       HttpServletRequest request){
        return kategoriProdukService.update(id,kategoriProdukService.mapMPToEntity(valKategoriProdukDTO),
                request);
    }

    // DELETE localhost:8080/kategoriProduk/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id,
                                         HttpServletRequest request){
        return kategoriProdukService.delete(id,request);
    }

    // GET localhost:8080/kategoriProduk/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id,
                                         HttpServletRequest request){
        return kategoriProdukService.findById(id,request);
    }

    // GET localhost:8080/kategoriProduk
    @GetMapping
    public ResponseEntity<Object> findAll(HttpServletRequest request){
        Pageable pageable = null;
        pageable = PageRequest.of(0,10, Sort.by("id"));// int int sort (asc)
        return kategoriProdukService.findAll(pageable,request);
    }

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

        return kategoriProdukService.findByParam(pageable,column,value,request);
    }

    @PostMapping("/upload-excel")
    public ResponseEntity<Object> uploadExcel(MultipartFile filez , HttpServletRequest request){
        return kategoriProdukService.uploadDataExcel(filez,request);
    }
    @GetMapping("/download-excel")
    public void downloadExcel(
            @RequestParam String column,
            @RequestParam String value,
            HttpServletRequest request,
            HttpServletResponse response
            ){
        kategoriProdukService.downloadReportExcel(column,value,request,response);
    }

    @GetMapping("/download-pdf")
    public void downloadPDF(
            @RequestParam String column,
            @RequestParam String value,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        kategoriProdukService.generateToPDF(column,value,request,response);
    }

    private String sortColumn(String column){
        switch (column){
            case "id":column="id";break;
            case "nama":column="nama";break;
            default:column="false";break;
        }
        return column;
    }
}