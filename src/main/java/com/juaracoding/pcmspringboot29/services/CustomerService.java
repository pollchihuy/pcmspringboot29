package com.juaracoding.pcmspringboot29.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.pcmspringboot29.core.IReport;
import com.juaracoding.pcmspringboot29.core.IService;
import com.juaracoding.pcmspringboot29.dto.CustomerDTO;
import com.juaracoding.pcmspringboot29.dto.respon.RespCustomerDTO;
import com.juaracoding.pcmspringboot29.handler.ResponseHandler;
import com.juaracoding.pcmspringboot29.model.Customer;
import com.juaracoding.pcmspringboot29.repo.CustomerRepo;
import com.juaracoding.pcmspringboot29.util.*;
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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
public class CustomerService implements IService<Customer>, IReport<Customer> {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransformPagination tf;

    private StringBuilder sBuild = new StringBuilder();

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private PdfGenerator pdfGenerator;

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
            LoggingFile.logException("CustomerService","public String welcomePage() line 29 "+ RequestCapture.allRequest(request),e);
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
            LoggingFile.logException("CustomerService","update(Long id, Customer customer, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
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
            LoggingFile.logException("CustomerService","update(Long id, Customer customer, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
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
            LoggingFile.logException("CustomerService","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
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
            LoggingFile.logException("CustomerService","update(Long id, Customer customer, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
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
            LoggingFile.logException("CustomerService","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
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
        String message = "";
        try{
            if(!ExcelReader.hasWorkBookFormat(multipartFile)){
                return new ResponseHandler().handleResponse("FORMAT HARUS EXCEL", HttpStatus.UNSUPPORTED_MEDIA_TYPE,null,
                        "MST10V061",request);
            }
            /** getDataMap untuk mengembalikan List<Map<String,String>> */
            List lt = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getDataMap();
            /** getAllData untuk mengembalikan String[][] dengan nama kolom */
            String [][] strArrDuaDimensi = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getAllData();
            /** getDataWithoutHeader untuk mengembalikan String[][] tanpa nama kolom */
            String [][] strArrDuaDimensiTanpaHeader = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getDataWithoutHeader();
            if(lt.isEmpty()){
                return new ResponseHandler().handleResponse("FILE EXCEL KOSONG", HttpStatus.BAD_REQUEST,null,
                        "MST10V062",request);
            }
            customerRepo.saveAll(convertListWorkBookToListEntity(lt,1L));
        }catch (Exception e){
            LoggingFile.logException("CustomerService","uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request)  LINE 225 " + RequestCapture.allRequest(request),e);
            return new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10E070",request);
        }
        return new ResponseHandler().handleResponse("DATA BERHASIL DISIMPAN", HttpStatus.CREATED,null,
                null,request);
    }

    @Override
    public List<Customer> convertListWorkBookToListEntity(List<Map<String, String>> workBookData, Long userId) {
//        workBookData isinya -> List<Map<String,Object>>

        List<Customer> list = new ArrayList<>();
        for (Map<String,String> m: workBookData) {
            Customer c = new Customer();
            c.setNama(m.get("NAMA LENGKAP"));
            c.setNoHp(m.get("NO HP"));
            c.setEmail(m.get("EMAIL"));
            c.setUsername(m.get("USERNAME"));
            c.setTanggalLahir(LocalDate.parse(m.get("TGL LAHIR")));
            c.setPassword(m.get("PASSWORD"));
            c.setTambahan(m.get("TAMBAHAN"));
            list.add(c);
        }
        return list;
    }

    @Override
    public void downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        /** yang di search menghasilkan 1000 data dan ditampilkan di web 100 data per halaman
         *  tapi dia lagi halaman ke 3
         *  tekan download
         */
        List list = null;
        try{
            switch (column){
                case "nama":list=customerRepo.findByNamaContains(value);break;
                case "noHp":list=customerRepo.findByNoHpContains(value);break;
                case "email":list=customerRepo.findByEmailContains(value);break;
                case "tanggalLahir":list=customerRepo.cariTanggal(value);break;
                case "umur":list=customerRepo.liatUmur(value);break;
                default:list=customerRepo.findAll();break;
            }
            if(list.isEmpty()){
                ResponseEntity<Object> respObject = new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN", HttpStatus.NOT_FOUND,null,
                        "MST10V071",request);
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());

                return ;
            }
            List<RespCustomerDTO> listCustomerDTO =  mapMPToDTO(list);
            String headerKey = "Content-Disposition";
            sBuild.setLength(0);
            String headerValue = sBuild.append("attachment; filename=customer_").
                    append(new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())).
                    append(".xlsx").toString();//customer_02012026_201312.xlsx
            response.setHeader("Cumi","Goreng");
            response.setHeader("Ayam","Tepung Mentega");
            response.setHeader("Gurame","Asam Manis");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader(headerKey, headerValue);
            String [] headerArr = new String[7];
            headerArr[0]="ID";
            headerArr[1]="Nama";
            headerArr[2]="Email";
            headerArr[3]="Tanggal Lahir";
            headerArr[4]="No HP";
            headerArr[5]="Tambahan";
            headerArr[6]="Umur";

            String [][] dataArr2D = new String[listCustomerDTO.size()][headerArr.length];
            for (int i = 0; i < listCustomerDTO.size(); i++) {
                dataArr2D[i][0]=listCustomerDTO.get(i).getId().toString();
                dataArr2D[i][1]=listCustomerDTO.get(i).getNama();
                dataArr2D[i][2]=listCustomerDTO.get(i).getEmail();
                dataArr2D[i][3]=listCustomerDTO.get(i).getTanggalLahir();
                dataArr2D[i][4]=listCustomerDTO.get(i).getNoHp();
                dataArr2D[i][5]=listCustomerDTO.get(i).getTambahan();
                dataArr2D[i][6]=listCustomerDTO.get(i).getUmur().toString();
            }
            new ExcelWriter(dataArr2D,headerArr,"Sheet1",response);
        }catch (Exception e){
            LoggingFile.logException("CustomerService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 291 " + RequestCapture.allRequest(request),e);
            ResponseEntity<Object> respObject = new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10E080",request);
            try {
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());
            } catch (IOException ex) {
                LoggingFile.logException("CustomerService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 300 " + RequestCapture.allRequest(request),e);
            }
            return ;
        }
    }

    @Override
    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        List list = null;
        try{
            switch (column){
                case "nama":list=customerRepo.findByNamaContains(value);break;
                case "noHp":list=customerRepo.findByNoHpContains(value);break;
                case "email":list=customerRepo.findByEmailContains(value);break;
                case "tanggalLahir":list=customerRepo.cariTanggal(value);break;
                case "umur":list=customerRepo.liatUmur(value);break;
                default:list=customerRepo.findAll();break;
            }
            if(list.isEmpty()){
                ResponseEntity<Object> respObject = new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN", HttpStatus.NOT_FOUND,null,
                        "MST10V081",request);
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());

                return ;
            }
            List<RespCustomerDTO> listCustomerDTO =  mapMPToDTO(list);
            Map<String,Object> mapResponse = new HashMap<>();
            Context context = new Context();
            String strHtml = null;

            mapResponse.put("title","REPORT DATA CUSTOMER");
            mapResponse.put("customers",listCustomerDTO);
            mapResponse.put("totalData",listCustomerDTO.size());
            mapResponse.put("username","Paul");

            context.setVariables(mapResponse);
            strHtml = springTemplateEngine.process("customer",context);
            pdfGenerator.htmlToPdf(strHtml,"customer",response);
        }catch (Exception e){
            LoggingFile.logException("CustomerService","generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 291 " + RequestCapture.allRequest(request),e);
            ResponseEntity<Object> respObject = new ResponseHandler().handleResponse("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR,null,
                    "MST10E090",request);
            try {
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());
            } catch (IOException ex) {
                LoggingFile.logException("CustomerService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 300 " + RequestCapture.allRequest(request),e);
            }
            return ;
        }
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

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if(object == null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}