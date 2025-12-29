package com.juaracoding.pcmspringboot29.services;


import com.juaracoding.pcmspringboot29.core.IReport;
import com.juaracoding.pcmspringboot29.core.IService;
import com.juaracoding.pcmspringboot29.dto.CustomerDTO;
import com.juaracoding.pcmspringboot29.handler.ResponseHandler;
import com.juaracoding.pcmspringboot29.model.Customer;
import com.juaracoding.pcmspringboot29.repo.CustomerRepo;
import com.juaracoding.pcmspringboot29.util.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * App Code - MST
 * Modul Code - 10
 * FV - FE (Failed Validation - Failed Exception)
 * EV (Error Validation)
 * EE (Error Exception)
 * E / V
 * MST10V013
 */
@Service
@Transactional
public class CustomerService implements IService<Customer>, IReport {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Object> save(Customer customer, HttpServletRequest request) {
        if(customer==null){
            return new ResponseHandler().handleResponse("GAGAL DISIMPAN", HttpStatus.BAD_REQUEST,null,
                    "MST10V001",request);
        }
//        if(customer.getId() == null){
//            return new ResponseHandler().handleResponse("GAGAL DISIMPAN", HttpStatus.BAD_REQUEST,null,
//                    "MST10V002",request);
//        }

        try {
            customerRepo.save(customer);
        }catch (Exception e){

            GlobalFunction.printConsole(e.getMessage());
            return new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10V010",request);
        }
        return new ResponseHandler().handleResponse("DATA BERHASIL DISIMPAN", HttpStatus.CREATED,null,
                null,request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, Customer customer, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> save(Customer customer, MultipartFile file, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> update(Long id, Customer customer, MultipartFile file, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request) {
        return null;
    }

    @Override
    public List convertListWorkBookToListEntity(List workBookData, Long userId) {
        return List.of();
    }

    @Override
    public void downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) {

    }

//    public Customer mapToEntity(CustomerDTO customerDTO){
//        Customer customer = new Customer();
//        customer.setUsername(customerDTO.getUsername());
//        customer.setPassword(customerDTO.getPassword());
//        customer.setEmail(customerDTO.getEmail());
//        customer.setNama(customerDTO.getNama());
//        customer.setNoHp(customerDTO.getNoHp());
//        customer.setTanggalLahir(LocalDate.parse(customerDTO.getTanggalLahir()));
//        return customer;
//    }
    public Customer mapMPToEntity(CustomerDTO customerDTO){

        Customer customer = modelMapper.map(customerDTO,Customer.class);
        return customer;
    }
}