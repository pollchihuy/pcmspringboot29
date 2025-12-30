package com.juaracoding.pcmspringboot29.services;


import com.juaracoding.pcmspringboot29.core.IReport;
import com.juaracoding.pcmspringboot29.core.IService;
import com.juaracoding.pcmspringboot29.dto.CustomerDTO;
import com.juaracoding.pcmspringboot29.dto.respon.RespCustomerDTO;
import com.juaracoding.pcmspringboot29.handler.ResponseHandler;
import com.juaracoding.pcmspringboot29.model.Customer;
import com.juaracoding.pcmspringboot29.repo.CustomerRepo;
import com.juaracoding.pcmspringboot29.util.GlobalFunction;
import com.juaracoding.pcmspringboot29.util.LoggingFile;
import com.juaracoding.pcmspringboot29.util.RequestCapture;
import com.juaracoding.pcmspringboot29.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private TransformPagination tf;

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
            LoggingFile.logException("CobaController","public String welcomePage() line 29 "+ RequestCapture.allRequest(request),e);
            return new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10V010",request);
        }
        return new ResponseHandler().handleResponse("DATA BERHASIL DISIMPAN", HttpStatus.CREATED,null,
                null,request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, Customer customer, HttpServletRequest request) {
        if(id ==null){
            return new ResponseHandler().handleResponse("GAGAL DIUBAH", HttpStatus.BAD_REQUEST,null,
                    "MST10V011",request);
        }
        if(customer==null){
            return new ResponseHandler().handleResponse("GAGAL DIUBAH", HttpStatus.BAD_REQUEST,null,
                    "MST10V012",request);
        }

        try{
            /**
             * customerRepo.findById(id) -> SELECT * FROM MstCustomer Where ID=?
             */
            Optional<Customer> optionalCustomer = customerRepo.findById(id);
            if(optionalCustomer.isEmpty()){
                return new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN", HttpStatus.BAD_REQUEST,null,
                        "MST10V013",request);
            }
            /** kalau data ditemukan masuk ke langkah ini */
            Customer customerDB  = optionalCustomer.get();
            /** UPDATE MstCustomer SET */
            customerDB.setTambahan(customer.getTambahan()); // tambahan = ?
            customerDB.setTanggalLahir(customer.getTanggalLahir());// tanggalLahir = ?
            customerDB.setNama(customer.getNama());// nama = ?
            customerDB.setPassword(customer.getPassword());// password = ?
            customerDB.setEmail(customer.getEmail());// password = ?
            customerDB.setNoHp(customer.getNoHp());// noHp = ?
        }catch (Exception e ){
            LoggingFile.logException("CobaController","update(Long id, Customer customer, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10E020",request);
        }
        return new ResponseHandler().handleResponse("DATA BERHASIL DIUBAH", HttpStatus.OK,null,
                null,request);
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        if(id ==null){
            return new ResponseHandler().handleResponse("GAGAL DIHAPUS", HttpStatus.BAD_REQUEST,null,
                    "MST10V021",request);
        }
        try{
            Optional<Customer> optionalCustomer = customerRepo.findById(id);
            if(optionalCustomer.isEmpty()){
                return new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN", HttpStatus.NOT_FOUND,null,
                        "MST10V022",request);
            }
            customerRepo.deleteById(id);
        }catch (Exception e ){
            LoggingFile.logException("CobaController","update(Long id, Customer customer, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10E030",request);
        }
        return new ResponseHandler().handleResponse("DATA BERHASIL DIHAPUS", HttpStatus.NO_CONTENT,null,
                null,request);
    }

    /** menyajikan data inisial dari sebuah menu */
    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page page = null;
        Map<String,Object> mapResponse = null;
        try{
            page = customerRepo.findAll(pageable);
            if(page.isEmpty()){
                return new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN", HttpStatus.NOT_FOUND,null,
                        "MST10V031",request);
            }
            mapResponse = tf.transformPagination(mapMPToDTO(page.getContent()),page,"id","");
        }catch (Exception e){
            LoggingFile.logException("CobaController","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
            return new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10E040",request);
        }

        return new ResponseHandler().handleResponse("DATA DITEMUKAN", HttpStatus.OK,mapResponse,
                null,request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Customer customerDB = null;
        if(id ==null){
            return new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN", HttpStatus.BAD_REQUEST,null,
                    "MST10V041",request);
        }
        try{
            Optional<Customer> optionalCustomer = customerRepo.findById(id);
            if(optionalCustomer.isEmpty()){
                return new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN", HttpStatus.BAD_REQUEST,null,
                        "MST10V042",request);
            }
           customerDB = optionalCustomer.get();
        }catch (Exception e ){
            LoggingFile.logException("CobaController","update(Long id, Customer customer, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10E050",request);
        }
        return new ResponseHandler().handleResponse("DATA DITEMUKAN", HttpStatus.OK,mapMPToDTO(customerDB),
                null,request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page page = null;
        Map<String,Object> mapResponse = null;
        try{
            switch (columnName){
                case "nama":page=customerRepo.findByNamaContains(pageable,value);break;
                case "noHp":page=customerRepo.findByNoHpContains(pageable,value);break;
                case "email":page=customerRepo.findByEmailContains(pageable,value);break;
                case "tanggalLahir":page=customerRepo.cariTanggal(pageable,value);break;
                case "umur":page=customerRepo.liatUmur(pageable,value);break;
                default:page=customerRepo.findAll(pageable);break;
            }
            if(page.isEmpty()){
                return new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN", HttpStatus.NOT_FOUND,null,
                        "MST10V031",request);
            }
            mapResponse = tf.transformPagination(mapMPToDTO(page.getContent()),page,"id","");
        }catch (Exception e){
            LoggingFile.logException("CobaController","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
            return new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10E040",request);
        }

        return new ResponseHandler().handleResponse("DATA DITEMUKAN", HttpStatus.OK,mapResponse,
                null,request);
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
    /** DTO untuk Request */
    public Customer mapMPToEntity(CustomerDTO customerDTO){

        Customer customer = modelMapper.map(customerDTO,Customer.class);
        customer.setTanggalLahir(LocalDate.parse(customerDTO.getTanggalLahir()));
        return customer;
    }

    /** ini DTO untuk response */
    public RespCustomerDTO mapMPToDTO(Customer customer){

        RespCustomerDTO customerDTO = modelMapper.map(customer,RespCustomerDTO.class);
        return customerDTO;
    }
    public List<RespCustomerDTO> mapMPToDTO(List<Customer> listCustomer){

        List<RespCustomerDTO> customerDTO = modelMapper.map(listCustomer,new TypeToken<List<RespCustomerDTO>>(){}.getType());
        return customerDTO;
    }
}