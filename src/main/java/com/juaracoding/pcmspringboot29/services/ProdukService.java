package com.juaracoding.pcmspringboot29.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.pcmspringboot29.core.IReport;
import com.juaracoding.pcmspringboot29.core.IService;
import com.juaracoding.pcmspringboot29.dto.respon.RespProdukDTO;
import com.juaracoding.pcmspringboot29.dto.validasi.ValProdukDTO;
import com.juaracoding.pcmspringboot29.model.KategoriProduk;
import com.juaracoding.pcmspringboot29.model.Produk;
import com.juaracoding.pcmspringboot29.repo.ProdukRepo;
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
 * Modul Code - 02
 * FV - FE (Failed Validation - Failed Exception)
 * EV (Error Validation)
 * EE (Error Exception)
 * E / V
 */
@Service
@Transactional
public class ProdukService implements IService<Produk>, IReport<Produk> {

    @Autowired
    private ProdukRepo produkRepo;

//    @Autowired
//    private LogProdukRepo logProdukRepo;

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
    public ResponseEntity<Object> save(Produk produk, HttpServletRequest request) {
        if(produk==null){
            return GlobalResponse.dataGagalDisimpan("MST02V001",request);
        }
        try {
            produkRepo.save(produk);
//            logProdukRepo.save(mapToLog(produk,"i",1L));
        }catch (Exception e){
            GlobalFunction.printConsole(e.getMessage());
            LoggingFile.logException("ProdukService","public String  save(Produk produk, HttpServletRequest request) line 29 "+ RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST02V010",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, Produk produk, HttpServletRequest request) {
        if(id ==null){
            return GlobalResponse.dataGagalDiubah("MST02V011",request);
        }
        if(produk==null){
            return GlobalResponse.dataGagalDiubah("MST02V012",request);
        }

        try{
            /**
             * produkRepo.findById(id) -> SELECT * FROM MstProduk Where ID=?
             */
            Optional<Produk> optionalProduk = produkRepo.findById(id);
            if(optionalProduk.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST02V013",request);
            }
            /** kalau data ditemukan masuk ke langkah ini */
            Produk produkDB  = optionalProduk.get();
            /** UPDATE MstProduk SET */
            produkDB.setNama(produk.getNama());// nama = ?
            produkDB.setKategoriProduk(produk.getKategoriProduk());
            produkDB.setSupplier(produk.getSupplier());
            produk.setId(id);
//            logProdukRepo.save(mapToLog(produk,"u",1L));
        }catch (Exception e ){
            LoggingFile.logException("ProdukService","update(Long id, Produk produk, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E020",request);
        }
        return GlobalResponse.dataBerhasilDiubah(request);
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        if(id ==null){
            return GlobalResponse.dataGagalDihapus("MST02V021",request);
        }
        try{
            Optional<Produk> optionalProduk = produkRepo.findById(id);
            if(optionalProduk.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST02V022",request);
            }
            produkRepo.deleteById(id);
//            logProdukRepo.save(mapToLog(optionalProduk.get(),"d",1L));
        }catch (Exception e ){
            LoggingFile.logException("ProdukService","update(Long id, Produk produk, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
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
            page = produkRepo.findAll(pageable);
            if(page.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST02V031",request);
            }
            mapResponse = tf.transformPagination(mapMPToDTO(page.getContent()),page,"id","");
        }catch (Exception e){
            LoggingFile.logException("ProdukService","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E040",request);
        }

        return GlobalResponse.dataDitemukan(mapResponse,request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Produk produkDB = null;
        if(id ==null){
            return GlobalResponse.dataTidakDitemukan("MST02V041",request);
        }
        try{
            Optional<Produk> optionalProduk = produkRepo.findById(id);
            if(optionalProduk.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST02V042",request);
            }
           produkDB = optionalProduk.get();
        }catch (Exception e ){
            LoggingFile.logException("ProdukService","update(Long id, Produk produk, HttpServletRequest request) LINE 81 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E050",request);
        }
        return GlobalResponse.dataDitemukan(mapMPToDTO(produkDB),request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page page = null;
        Map<String,Object> mapResponse = null;
        try{
            switch (columnName){
                case "nama":page=produkRepo.findByNamaContains(pageable,value);break;
                default:page=produkRepo.findAll(pageable);break;
            }
            if(page.isEmpty()){
                return GlobalResponse.dataTidakDitemukan("MST02V031",request);
            }
            mapResponse = tf.transformPagination(mapMPToDTO(page.getContent()),page,"id","");
        }catch (Exception e){
            LoggingFile.logException("ProdukService","findAll(Pageable pageable, HttpServletRequest request) LINE 140 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E040",request);
        }

        return GlobalResponse.dataDitemukan(mapResponse,request);
    }

    @Override
    public ResponseEntity<Object> save(Produk produk, MultipartFile file, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> update(Long id, Produk produk, MultipartFile file, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request) {
        String message = "";
        try{
            if(!ExcelReader.hasWorkBookFormat(multipartFile)){
                return GlobalResponse.formatHarusExcel("MST02V061",request);
            }
            /** getDataMap untuk mengembalikan List<Map<String,String>> */
            List lt = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getDataMap();
            /** getAllData untuk mengembalikan String[][] dengan nama kolom */
            String [][] strArrDuaDimensi = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getAllData();
            /** getDataWithoutHeader untuk mengembalikan String[][] tanpa nama kolom */
            String [][] strArrDuaDimensiTanpaHeader = new ExcelReader(multipartFile.getInputStream(),"Sheet1").getDataWithoutHeader();
            if(lt.isEmpty()){
                return GlobalResponse.fileExcelKosong("MST02V062",request);
            }
            Long userId = 1L;
            List<Produk> listProduk = convertListWorkBookToListEntity(lt,userId);
            produkRepo.saveAll(listProduk);
//            logProdukRepo.saveAll(mapToLog(listProduk,"i",userId));
        }catch (Exception e){
            LoggingFile.logException("ProdukService","uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request)  LINE 225 " + RequestCapture.allRequest(request),e);
            return GlobalResponse.terjadiKesalahan("MST10E070",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public List<Produk> convertListWorkBookToListEntity(List<Map<String, String>> workBookData, Long userId) {
//        workBookData isinya -> List<Map<String,Object>>

        List<Produk> list = new ArrayList<>();
        for (Map<String,String> m: workBookData) {
            Produk c = new Produk();
            c.setNama(m.get("NAMA PRODUK"));
            KategoriProduk kategoriProduk = new KategoriProduk();
            kategoriProduk.setId(Long.parseLong(m.get("ID KATEGORI")));
            c.setKategoriProduk(kategoriProduk);
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
                case "nama":list=produkRepo.findByNamaContains(value);break;
                default:list=produkRepo.findAll();break;
            }
            if(list.isEmpty()){
                ResponseEntity<Object> respObject =
                        GlobalResponse.dataTidakDitemukan("MST02V071",request);
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());

                return ;
            }
            List<RespProdukDTO> listProdukDTO =  mapMPToDTO(list);
            String headerKey = "Content-Disposition";
            sBuild.setLength(0);
            String headerValue = sBuild.append("attachment; filename=produk_").
                    append(new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date())).
                    append(".xlsx").toString();//produk_02012026_201312.xlsx
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader(headerKey, headerValue);
            Map<String,Object> map = GlobalFunction.convertClassToMap(new RespProdukDTO());
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
            int intListDTOSize = listProdukDTO.size();
            String [][] dataArr2D = new String[intListDTOSize][headerArr.length];
            for (int i = 0; i < intListDTOSize; i++) {
                map = GlobalFunction.convertClassToMap(listProdukDTO.get(i));
                for (int j = 0; j < intListTemp; j++) {
                    dataArr2D[i][j] = String.valueOf(map.get(loopDataArr[j]));
                }
            }
            new ExcelWriter(dataArr2D,headerArr,"Sheet1",response);
        }catch (Exception e){
            LoggingFile.logException("ProdukService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 291 " + RequestCapture.allRequest(request),e);
            ResponseEntity<Object> respObject =
                    GlobalResponse.terjadiKesalahan("MST10E080",request);
            try {
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());
            } catch (IOException ex) {
                LoggingFile.logException("ProdukService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 300 " + RequestCapture.allRequest(request),e);
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
                case "nama":list=produkRepo.findByNamaContains(value);break;
                default:list=produkRepo.findAll();break;
            }
            if(list.isEmpty()){
                ResponseEntity<Object> respObject =
                        GlobalResponse.dataTidakDitemukan("MST02V081",request);
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());

                return ;
            }
            List<RespProdukDTO> listProdukDTO =  mapMPToDTO(list);
            Map<String,Object> mapColumnName = GlobalFunction.convertClassToMap(new RespProdukDTO());
            List<String> listTemp = new ArrayList<>();// ini nama kolom
            List<String> listHelper = new ArrayList<>();// ini untuk mengekstrak value nya
            for (Map.Entry<String,Object> m:mapColumnName.entrySet()) {
                listTemp.add(GlobalFunction.camelToStandard(m.getKey()));
                listHelper.add(m.getKey());
            }
            int intRepProdukDTOSize = listProdukDTO.size();
            Map<String,Object> mapTemp = null;
            List<Map<String,Object>> listMap = new ArrayList<>();

            /** List<RespProdukDTO> ->> List<Map<String,Object>> */
            for (int i = 0; i < intRepProdukDTOSize; i++) {
                mapTemp = GlobalFunction.convertClassToMap(listProdukDTO.get(i));
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
            mapResponse.put("totalData", intRepProdukDTOSize);
            mapResponse.put("username","Paul");

            context.setVariables(mapResponse);
            strHtml = springTemplateEngine.process("global-report",context);
            pdfGenerator.htmlToPdf(strHtml,"produk",response);
        }catch (Exception e){
            LoggingFile.logException("ProdukService","generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 291 " + RequestCapture.allRequest(request),e);
            ResponseEntity<Object> respObject =
                    GlobalResponse.terjadiKesalahan("MST10E090",request);
            try {
                response.getWriter().write(convertObjectToJson(respObject));
                response.setStatus(respObject.getStatusCodeValue());
            } catch (IOException ex) {
                LoggingFile.logException("ProdukService","downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) LINE 300 " + RequestCapture.allRequest(request),e);
            }
            return ;
        }
    }

//    public Produk mapToEntity(ProdukDTO produkDTO){
//        Produk produk = new Produk();
//        produk.setUsername(produkDTO.getUsername());
//        produk.setPassword(produkDTO.getPassword());
//        produk.setEmail(produkDTO.getEmail());
//        produk.setNama(produkDTO.getNama());
//        produk.setNoHp(produkDTO.getNoHp());
//        produk.setTanggalLahir(LocalDate.parse(produkDTO.getTanggalLahir()));
//        return produk;
//    }
    /** DTO untuk Request */
    public Produk mapMPToEntity(ValProdukDTO valProdukDTO){
        Produk produk = modelMapper.map(valProdukDTO,Produk.class);
        return produk;
    }

    /** ini DTO untuk response */
    public RespProdukDTO mapMPToDTO(Produk produk){

        RespProdukDTO produkDTO = modelMapper.map(produk,RespProdukDTO.class);
        produkDTO.setNamaKategoriProduk(produk.getKategoriProduk().getNama());
        return produkDTO;
    }

//    public LogProduk mapToLog(Produk produk,String flag,Long userId){
//
//        LogProduk logProduk = new LogProduk();
//        logProduk.setFlag(flag);
//        logProduk.setCreatedBy(userId);
//        logProduk.setNama(produk.getNama());
//        logProduk.setIdProduk(produk.getId());
//        return logProduk;
//    }

//    public List<LogProduk> mapToLog(List<Produk> listProduk,String flag,Long userId){
//
//        List<LogProduk> l = new ArrayList<>();
//        for (Produk produk:
//             listProduk) {
//            LogProduk logProduk = new LogProduk();
//            logProduk.setFlag(flag);
//            logProduk.setCreatedBy(userId);
//            logProduk.setNama(produk.getNama());
//            logProduk.setIdProduk(produk.getId());
//            l.add(logProduk);
//        }
//
//        return l;
//    }


    public List<RespProdukDTO> mapMPToDTO(List<Produk> listProduk){
//        List<RespProdukDTO> produkDTO = modelMapper.map(listProduk,new TypeToken<List<RespProdukDTO>>(){}.getType());
        List<RespProdukDTO> produkDTO = new ArrayList<>();
        for (Produk p :
             listProduk) {
            RespProdukDTO respProdukDTO = new RespProdukDTO();
            respProdukDTO.setId(p.getId());
            respProdukDTO.setNama(p.getNama());
            respProdukDTO.setNamaKategoriProduk(p.getKategoriProduk().getNama());
            produkDTO.add(respProdukDTO);
        }
        return produkDTO;
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if(object == null){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}