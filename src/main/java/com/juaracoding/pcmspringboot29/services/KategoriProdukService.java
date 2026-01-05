package com.juaracoding.pcmspringboot29.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.pcmspringboot29.core.IReport;
import com.juaracoding.pcmspringboot29.core.IService;
import com.juaracoding.pcmspringboot29.dto.respon.RespKategoriProdukDTO;
import com.juaracoding.pcmspringboot29.dto.validasi.ValKategoriProdukDTO;
import com.juaracoding.pcmspringboot29.handler.ResponseHandler;
import com.juaracoding.pcmspringboot29.model.KategoriProduk;
import com.juaracoding.pcmspringboot29.model.LogKategoriProduk;
import com.juaracoding.pcmspringboot29.repo.KategoriProdukRepo;
import com.juaracoding.pcmspringboot29.repo.LogKategoriProdukRepo;
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
 * Modul Code - 01
 * FV - FE (Failed Validation - Failed Exception)
 * EV (Error Validation)
 * EE (Error Exception)
 * E / V
 */
@Service
@Transactional
public class KategoriProdukService implements IService<KategoriProduk>, IReport<KategoriProduk> {

    @Autowired
    private KategoriProdukRepo kategoriProdukRepo;

    @Autowired
    private LogKategoriProdukRepo logKategoriProdukRepo;

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
    public ResponseEntity<Object> save(KategoriProduk kategoriProduk, HttpServletRequest request) {
        if(kategoriProduk==null){
            return GlobalResponse.dataGagalDisimpan("MST01V001",request);
        }
        try {
            kategoriProdukRepo.save(kategoriProduk);
            logKategoriProdukRepo.save(mapToLog(kategoriProduk,"i",1L));
        }catch (Exception e){
            GlobalFunction.printConsole(e.getMessage());
            LoggingFile.logException("KategoriProdukService","public String  save(KategoriProduk kategoriProduk, HttpServletRequest request) line 29 "+ RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST01V010",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, KategoriProduk kategoriProduk, HttpServletRequest request) {
        if(id ==null){
            return GlobalResponse.dataGagalDiubah("MST01V011",request);
        }
        if(kategoriProduk==null){
            return GlobalResponse.dataGagalDiubah("MST01V012",request);
        }

        try{
            /**
             * kategoriProdukRepo.findById(id) -> SELECT * FROM MstKategoriProduk Where ID=?
             */
            Optional<KategoriProduk> optionalKategoriProduk = kategoriProdukRepo.findById(id);
            if(optionalKategoriProduk.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST01V013",request);
            }
            /** kalau data ditemukan masuk ke langkah ini */
            KategoriProduk kategoriProdukDB  = optionalKategoriProduk.get();
            /** UPDATE MstKategoriProduk SET */
            kategoriProdukDB.setNama(kategoriProduk.getNama());// nama = ?
            kategoriProduk.setId(id);
            logKategoriProdukRepo.save(mapToLog(kategoriProduk,"u",1L));
        }catch (Exception e ){
            LoggingFile.logException("KategoriProdukService","update(Long id, KategoriProduk kategoriProduk, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E020",request);
        }
        return GlobalResponse.dataBerhasilDiubah(request);
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        if(id ==null){
            return GlobalResponse.dataGagalDihapus("MST01V021",request);
        }
        try{
            Optional<KategoriProduk> optionalKategoriProduk = kategoriProdukRepo.findById(id);
            if(optionalKategoriProduk.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST01V022",request);
            }
            kategoriProdukRepo.deleteById(id);
            logKategoriProdukRepo.save(mapToLog(optionalKategoriProduk.get(),"d",1L));
        }catch (Exception e ){
            LoggingFile.logException("KategoriProdukService","update(Long id, KategoriProduk kategoriProduk, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
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
            page = kategoriProdukRepo.findAll(pageable);
            if(page.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST01V031",request);
            }
            mapResponse = tf.transformPagination(mapMPToDTO(page.getContent()),page,"id","");
        }catch (Exception e){
            LoggingFile.logException("KategoriProdukService","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E040",request);
        }

        return GlobalResponse.dataDitemukan(mapResponse,request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        KategoriProduk kategoriProdukDB = null;
        if(id ==null){
            return GlobalResponse.dataTidakDitemukan("MST01V041",request);
        }
        try{
            Optional<KategoriProduk> optionalKategoriProduk = kategoriProdukRepo.findById(id);
            if(optionalKategoriProduk.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST01V042",request);
            }
           kategoriProdukDB = optionalKategoriProduk.get();
        }catch (Exception e ){
            LoggingFile.logException("KategoriProdukService","update(Long id, KategoriProduk kategoriProduk, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E050",request);
        }
        return GlobalResponse.dataDitemukan(mapMPToDTO(kategoriProdukDB),request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page page = null;
        Map<String,Object> mapResponse = null;
        try{
            switch (columnName){
                case "nama":page=kategoriProdukRepo.findByNamaContains(pageable,value);break;
                default:page=kategoriProdukRepo.findAll(pageable);break;
            }
            if(page.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST01V031",request);
            }
            mapResponse = tf.transformPagination(mapMPToDTO(page.getContent()),page,"id","");
        }catch (Exception e){
            LoggingFile.logException("KategoriProdukService","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E040",request);
        }

        return GlobalResponse.dataDitemukan(mapResponse,request);
    }

    @Override
    public ResponseEntity<Object> save(KategoriProduk kategoriProduk, MultipartFile file, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> update(Long id, KategoriProduk kategoriProduk, MultipartFile file, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request) {
        String message = "";
        try{
            if(!ExcelReader.hasWorkBookFormat(multipartFile)){
                return GlobalResponse.formatHarusExcel("MST01V061",request);
            }
            /** getDataMap untuk mengembalikan List<Map<String,String>> */
            List lt = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getDataMap();
            /** getAllData untuk mengembalikan String[][] dengan nama kolom */
            String [][] strArrDuaDimensi = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getAllData();
            /** getDataWithoutHeader untuk mengembalikan String[][] tanpa nama kolom */
            String [][] strArrDuaDimensiTanpaHeader = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getDataWithoutHeader();
            if(lt.isEmpty()){
                return GlobalResponse.fileExcelKosong("MST01V062",request);
            }
            Long userId = 1L;
            List<KategoriProduk> listKategoriProduk = convertListWorkBookToListEntity(lt,userId);
            kategoriProdukRepo.saveAll(listKategoriProduk);
            logKategoriProdukRepo.saveAll(mapToLog(listKategoriProduk,"i",userId));
        }catch (Exception e){
            LoggingFile.logException("KategoriProdukService","uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request)  LINE 225 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E070",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public List<KategoriProduk> convertListWorkBookToListEntity(List<Map<String, String>> workBookData, Long userId) {
//        workBookData isinya -> List<Map<String,Object>>

        List<KategoriProduk> list = new ArrayList<>();
        for (Map<String,String> m: workBookData) {
            KategoriProduk c = new KategoriProduk();
            c.setNama(m.get("NAMA KATEGORI PRODUK"));
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
                case "nama":list=kategoriProdukRepo.findByNamaContains(value);break;
                default:list=kategoriProdukRepo.findAll();break;
            }
            if(list.isEmpty()){
                ResponseEntity<Object> respObject =
                        GlobalResponse.dataTidakDitemukan("MST01V071",request);
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());

                return ;
            }
            List<RespKategoriProdukDTO> listKategoriProdukDTO =  mapMPToDTO(list);
            String headerKey = "Content-Disposition";
            sBuild.setLength(0);
            String headerValue = sBuild.append("attachment; filename=kategoriProduk_").
                    append(new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())).
                    append(".xlsx").toString();//kategoriProduk_02012026_201312.xlsx
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader(headerKey, headerValue);
            Map<String,Object> map = GlobalFunction.convertClassToMap(new RespKategoriProdukDTO());
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
            int intListDTOSize = listKategoriProdukDTO.size();
            String [][] dataArr2D = new String[intListDTOSize][headerArr.length];
            for (int i = 0; i < intListDTOSize; i++) {
                map = GlobalFunction.convertClassToMap(listKategoriProdukDTO.get(i));
                for (int j = 0; j < intListTemp; j++) {
                    dataArr2D[i][j] = String.valueOf(map.get(loopDataArr[j]));
                }
            }
            new ExcelWriter(dataArr2D,headerArr,"Sheet1",response);
        }catch (Exception e){
            LoggingFile.logException("KategoriProdukService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 291 " + RequestCapture.allRequest(request),e);
            ResponseEntity<Object> respObject =
                    GlobalResponse.terjadiKesalahan("MST10E080",request);
            try {
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());
            } catch (IOException ex) {
                LoggingFile.logException("KategoriProdukService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 300 " + RequestCapture.allRequest(request),e);
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
                case "nama":list=kategoriProdukRepo.findByNamaContains(value);break;
                default:list=kategoriProdukRepo.findAll();break;
            }
            if(list.isEmpty()){
                ResponseEntity<Object> respObject =
                        GlobalResponse.dataTidakDitemukan("MST01V081",request);
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());

                return ;
            }
            List<RespKategoriProdukDTO> listKategoriProdukDTO =  mapMPToDTO(list);
            Map<String,Object> mapColumnName = GlobalFunction.convertClassToMap(new RespKategoriProdukDTO());
            List<String> listTemp = new ArrayList<>();// ini nama kolom
            List<String> listHelper = new ArrayList<>();// ini untuk mengekstrak value nya
            for (Map.Entry<String,Object> m:mapColumnName.entrySet()) {
                listTemp.add(GlobalFunction.camelToStandard(m.getKey()));
                listHelper.add(m.getKey());
            }
            int intRepKategoriProdukDTOSize = listKategoriProdukDTO.size();
            Map<String,Object> mapTemp = null;
            List<Map<String,Object>> listMap = new ArrayList<>();

            /** List<RespKategoriProdukDTO> ->> List<Map<String,Object>> */
            for (int i = 0; i < intRepKategoriProdukDTOSize; i++) {
                mapTemp = GlobalFunction.convertClassToMap(listKategoriProdukDTO.get(i));
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
            mapResponse.put("totalData", intRepKategoriProdukDTOSize);
            mapResponse.put("username","Paul");

            context.setVariables(mapResponse);
            strHtml = springTemplateEngine.process("global-report",context);
            pdfGenerator.htmlToPdf(strHtml,"kategoriProduk",response);
        }catch (Exception e){
            LoggingFile.logException("KategoriProdukService","generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 291 " + RequestCapture.allRequest(request),e);
            ResponseEntity<Object> respObject =
                    GlobalResponse.terjadiKesalahan("MST10E090",request);
            try {
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());
            } catch (IOException ex) {
                LoggingFile.logException("KategoriProdukService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 300 " + RequestCapture.allRequest(request),e);
            }
            return ;
        }
    }

//    public KategoriProduk mapToEntity(KategoriProdukDTO kategoriProdukDTO){
//        KategoriProduk kategoriProduk = new KategoriProduk();
//        kategoriProduk.setUsername(kategoriProdukDTO.getUsername());
//        kategoriProduk.setPassword(kategoriProdukDTO.getPassword());
//        kategoriProduk.setEmail(kategoriProdukDTO.getEmail());
//        kategoriProduk.setNama(kategoriProdukDTO.getNama());
//        kategoriProduk.setNoHp(kategoriProdukDTO.getNoHp());
//        kategoriProduk.setTanggalLahir(LocalDate.parse(kategoriProdukDTO.getTanggalLahir()));
//        return kategoriProduk;
//    }
    /** DTO untuk Request */
    public KategoriProduk mapMPToEntity(ValKategoriProdukDTO valKategoriProdukDTO){
        KategoriProduk kategoriProduk = modelMapper.map(valKategoriProdukDTO,KategoriProduk.class);
        return kategoriProduk;
    }

    /** ini DTO untuk response */
    public RespKategoriProdukDTO mapMPToDTO(KategoriProduk kategoriProduk){

        RespKategoriProdukDTO kategoriProdukDTO = modelMapper.map(kategoriProduk,RespKategoriProdukDTO.class);
        return kategoriProdukDTO;
    }

    public LogKategoriProduk mapToLog(KategoriProduk kategoriProduk,String flag,Long userId){

        LogKategoriProduk logKategoriProduk = new LogKategoriProduk();
        logKategoriProduk.setFlag(flag);
        logKategoriProduk.setCreatedBy(userId);
        logKategoriProduk.setNama(kategoriProduk.getNama());
        logKategoriProduk.setIdKategoriProduk(kategoriProduk.getId());
        return logKategoriProduk;
    }

    public List<LogKategoriProduk> mapToLog(List<KategoriProduk> listKategoriProduk,String flag,Long userId){

        List<LogKategoriProduk> l = new ArrayList<>();
        for (KategoriProduk kategoriProduk:
             listKategoriProduk) {
            LogKategoriProduk logKategoriProduk = new LogKategoriProduk();
            logKategoriProduk.setFlag(flag);
            logKategoriProduk.setCreatedBy(userId);
            logKategoriProduk.setNama(kategoriProduk.getNama());
            logKategoriProduk.setIdKategoriProduk(kategoriProduk.getId());
            l.add(logKategoriProduk);
        }

        return l;
    }


    public List<RespKategoriProdukDTO> mapMPToDTO(List<KategoriProduk> listKategoriProduk){

        List<RespKategoriProdukDTO> kategoriProdukDTO = modelMapper.map(listKategoriProduk,new TypeToken<List<RespKategoriProdukDTO>>(){}.getType());
        return kategoriProdukDTO;
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if(object == null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}