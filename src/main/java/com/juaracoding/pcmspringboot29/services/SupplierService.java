package com.juaracoding.pcmspringboot29.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.pcmspringboot29.core.IReport;
import com.juaracoding.pcmspringboot29.core.IService;
import com.juaracoding.pcmspringboot29.dto.respon.RespSupplierDTO;
import com.juaracoding.pcmspringboot29.dto.validasi.ValSupplierDTO;
import com.juaracoding.pcmspringboot29.model.Supplier;
import com.juaracoding.pcmspringboot29.repo.SupplierRepo;
import com.juaracoding.pcmspringboot29.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * App Code - MST
 * Modul Code - 03
 * FV - FE (Failed Validation - Failed Exception)
 * EV (Error Validation)
 * EE (Error Exception)
 * E / V
 */
@Service
@Transactional
public class SupplierService implements IService<Supplier>, IReport<Supplier> {

    @Autowired
    private SupplierRepo supplierRepo;

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
    public ResponseEntity<Object> save(Supplier supplier, HttpServletRequest request) {
        if(supplier==null){
            return GlobalResponse.dataGagalDisimpan("MS03V001",request);
        }
        try {
            supplierRepo.save(supplier);
        }catch (Exception e){
            GlobalFunction.printConsole(e.getMessage());
            LoggingFile.logException("SupplierService","public String  save(Supplier supplier, HttpServletRequest request) line 29 "+ RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MS03V010",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, Supplier supplier, HttpServletRequest request) {
        if(id ==null){
            return GlobalResponse.dataGagalDiubah("MS03V011",request);
        }
        if(supplier==null){
            return GlobalResponse.dataGagalDiubah("MS03V012",request);
        }

        try{
            /**
             * supplierRepo.findById(id) -> SELECT * FROM MstSupplier Where ID=?
             */
            Optional<Supplier> optionalSupplier = supplierRepo.findById(id);
            if(optionalSupplier.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MS03V013",request);
            }
            /** kalau data ditemukan masuk ke langkah ini */
            Supplier supplierDB  = optionalSupplier.get();
            /** UPDATE MstSupplier SET */
            supplierDB.setNama(supplier.getNama());// nama = ?
            supplier.setId(id);
        }catch (Exception e ){
            LoggingFile.logException("SupplierService","update(Long id, Supplier supplier, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E020",request);
        }
        return GlobalResponse.dataBerhasilDiubah(request);
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        if(id ==null){
            return GlobalResponse.dataGagalDihapus("MS03V021",request);
        }
        try{
            Optional<Supplier> optionalSupplier = supplierRepo.findById(id);
            if(optionalSupplier.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MS03V022",request);
            }
            supplierRepo.deleteById(id);
        }catch (Exception e ){
            LoggingFile.logException("SupplierService","update(Long id, Supplier supplier, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E030",request);
        }
        return GlobalResponse.dataBerhasilDihapus(request);
    }

    /** menyajikan data inisial dari sebuah menu */
    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page page = null;
        Map<String,Object> mapResponse = null;
        try{
            page = supplierRepo.findAll(pageable);
            if(page.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MS03V031",request);
            }
            mapResponse = tf.transformPagination(mapMPToDTO(page.getContent()),page,"id","");
        }catch (Exception e){
            LoggingFile.logException("SupplierService","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E040",request);
        }

        return GlobalResponse.dataDitemukan(mapResponse,request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Supplier supplierDB = null;
        if(id ==null){
            return GlobalResponse.dataTidakDitemukan("MS03V041",request);
        }
        try{
            Optional<Supplier> optionalSupplier = supplierRepo.findById(id);
            if(optionalSupplier.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MS03V042",request);
            }
           supplierDB = optionalSupplier.get();
        }catch (Exception e ){
            LoggingFile.logException("SupplierService","update(Long id, Supplier supplier, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E050",request);
        }
        return GlobalResponse.dataDitemukan(mapMPToDTO(supplierDB),request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page page = null;
        Map<String,Object> mapResponse = null;
        try{
            switch (columnName){
                case "nama":page=supplierRepo.findByNamaContains(pageable,value);break;
                default:page=supplierRepo.findAll(pageable);break;
            }
            if(page.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MS03V031",request);
            }
            mapResponse = tf.transformPagination(mapMPToDTO(page.getContent()),page,"id","");
        }catch (Exception e){
            LoggingFile.logException("SupplierService","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E040",request);
        }

        return GlobalResponse.dataDitemukan(mapResponse,request);
    }

    @Override
    public ResponseEntity<Object> save(Supplier supplier, MultipartFile file, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> update(Long id, Supplier supplier, MultipartFile file, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request) {
        String message = "";
        try{
            if(!ExcelReader.hasWorkBookFormat(multipartFile)){
                return GlobalResponse.formatHarusExcel("MS03V061",request);
            }
            /** getDataMap untuk mengembalikan List<Map<String,String>> */
            List lt = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getDataMap();
            /** getAllData untuk mengembalikan String[][] dengan nama kolom */
            String [][] strArrDuaDimensi = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getAllData();
            /** getDataWithoutHeader untuk mengembalikan String[][] tanpa nama kolom */
            String [][] strArrDuaDimensiTanpaHeader = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getDataWithoutHeader();
            if(lt.isEmpty()){
                return GlobalResponse.fileExcelKosong("MS03V062",request);
            }
            Long userId = 1L;
            List<Supplier> listSupplier = convertListWorkBookToListEntity(lt,userId);
            supplierRepo.saveAll(listSupplier);
        }catch (Exception e){
            LoggingFile.logException("SupplierService","uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request)  LINE 225 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E070",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public List<Supplier> convertListWorkBookToListEntity(List<Map<String, String>> workBookData, Long userId) {
//        workBookData isinya -> List<Map<String,Object>>

        List<Supplier> list = new ArrayList<>();
        for (Map<String,String> m: workBookData) {
            Supplier c = new Supplier();
            c.setNama(m.get("NAMA SUPPLIER"));
            list.add(c);
        }
        return list;
    }

    @Override
    public void downloadReportExcel(String column, String value, HttpServletRequest request,
                                    HttpServletResponse response) {
        List list = null;
        try{
            switch (column){
                case "nama":list=supplierRepo.findByNamaContains(value);break;
                default:list=supplierRepo.findAll();break;
            }
            if(list.isEmpty()){
                ResponseEntity<Object> respObject =
                        GlobalResponse.dataTidakDitemukan("MS03V071",request);
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());

                return ;
            }
            List<RespSupplierDTO> listSupplierDTO =  mapMPToDTO(list);
            String headerKey = "Content-Disposition";
            sBuild.setLength(0);
            String headerValue = sBuild.append("attachment; filename=supplier_").
                    append(new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())).
                    append(".xlsx").toString();//supplier_02012026_201312.xlsx
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader(headerKey, headerValue);
            Map<String,Object> map = GlobalFunction.convertClassToMap(new RespSupplierDTO());
            List<String> listTemp = new ArrayList<>();
            for (Map.Entry<String,Object> entry : map.entrySet()) {
                listTemp.add(entry.getKey());
            }
            int intListTemp = listTemp.size();
            String [] headerArr = new String[intListTemp];//kolom judul di excel
            String [] loopDataArr = new String[intListTemp];//kolom judul java reflection

            for (int i = 0; i < intListTemp; i++) {
                /** contoh : tanggalLahir -> Tanggal Lahir / TANGGAL LAHIR*/
                headerArr[i] = GlobalFunction.camelToStandard(listTemp.get(i));// id-> ID , nama -> Nama , tanggalLahir -> Tanggal Lahir
                loopDataArr[i] = listTemp.get(i);
            }
            int intListDTOSize = listSupplierDTO.size();
            String [][] dataArr2D = new String[intListDTOSize][headerArr.length];
            for (int i = 0; i < intListDTOSize; i++) {
                map = GlobalFunction.convertClassToMap(listSupplierDTO.get(i));
                for (int j = 0; j < intListTemp; j++) {
                    dataArr2D[i][j] = String.valueOf(map.get(loopDataArr[j]));
                }
            }
            new ExcelWriter(dataArr2D,headerArr,"Sheet1",response);
        }catch (Exception e){
            LoggingFile.logException("SupplierService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 291 " + RequestCapture.allRequest(request),e);
            ResponseEntity<Object> respObject =
                    GlobalResponse.terjadiKesalahan("MST10E080",request);
            try {
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());
            } catch (IOException ex) {
                LoggingFile.logException("SupplierService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 300 " + RequestCapture.allRequest(request),e);
            }
            return ;
        }
    }

    @Override
    public void generateToPDF(String column, String value, HttpServletRequest request,
                              HttpServletResponse response) {
        List list = null;
        try{
            switch (column){
                case "nama":list=supplierRepo.findByNamaContains(value);break;
                default:list=supplierRepo.findAll();break;
            }
            if(list.isEmpty()){
                ResponseEntity<Object> respObject =
                        GlobalResponse.dataTidakDitemukan("MS03V081",request);
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());

                return ;
            }
            List<RespSupplierDTO> listSupplierDTO =  mapMPToDTO(list);
            Map<String,Object> mapColumnName = GlobalFunction.convertClassToMap(new RespSupplierDTO());
            List<String> listTemp = new ArrayList<>();// ini nama kolom
            List<String> listHelper = new ArrayList<>();// ini untuk mengekstrak value nya
            for (Map.Entry<String,Object> m:mapColumnName.entrySet()) {
                listTemp.add(GlobalFunction.camelToStandard(m.getKey()));
                listHelper.add(m.getKey());
            }
            int intRepSupplierDTOSize = listSupplierDTO.size();
            Map<String,Object> mapTemp = null;
            List<Map<String,Object>> listMap = new ArrayList<>();

            /** List<RespSupplierDTO> ->> List<Map<String,Object>> */
            for (int i = 0; i < intRepSupplierDTOSize; i++) {
                mapTemp = GlobalFunction.convertClassToMap(listSupplierDTO.get(i));
                listMap.add(mapTemp);
            }

            Map<String,Object> mapResponse = new HashMap<>();
            Context context = new Context();
            String strHtml = null;

            mapResponse.put("title","REPORT DATA KATEGORI PRODUK");
            mapResponse.put("listKolom",listTemp);
            mapResponse.put("timestamp",new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(new Date()));
            mapResponse.put("listHelper",listHelper);
            mapResponse.put("listContent",listMap);
            mapResponse.put("totalData", intRepSupplierDTOSize);
            mapResponse.put("username","Paul");

            context.setVariables(mapResponse);
            strHtml = springTemplateEngine.process("global-report",context);
            pdfGenerator.htmlToPdf(strHtml,"supplier",response);
        }catch (Exception e){
            LoggingFile.logException("SupplierService","generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 291 " + RequestCapture.allRequest(request),e);
            ResponseEntity<Object> respObject =
                    GlobalResponse.terjadiKesalahan("MST10E090",request);
            try {
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());
            } catch (IOException ex) {
                LoggingFile.logException("SupplierService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 300 " + RequestCapture.allRequest(request),e);
            }
            return ;
        }
    }

    /** DTO untuk Request */
    public Supplier mapMPToEntity(ValSupplierDTO valSupplierDTO){
        Supplier supplier = modelMapper.map(valSupplierDTO,Supplier.class);
        return supplier;
    }

    /** ini DTO untuk response */
    public RespSupplierDTO mapMPToDTO(Supplier supplier){

        RespSupplierDTO supplierDTO = modelMapper.map(supplier,RespSupplierDTO.class);
        return supplierDTO;
    }

    public List<RespSupplierDTO> mapMPToDTO(List<Supplier> listSupplier){

        List<RespSupplierDTO> supplierDTO = modelMapper.map(listSupplier,new TypeToken<List<RespSupplierDTO>>(){}.getType());
        return supplierDTO;
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if(object == null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}