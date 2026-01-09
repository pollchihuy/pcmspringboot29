package com.juaracoding.pcmspringboot29.controller;

import com.juaracoding.pcmspringboot29.config.OtherConfig;
import com.juaracoding.pcmspringboot29.model.KategoriProduk;
import com.juaracoding.pcmspringboot29.repo.KategoriProdukRepo;
import com.juaracoding.pcmspringboot29.utils.DataGenerator;
import com.juaracoding.pcmspringboot29.utils.TokenGenerator;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class KategoriProdukControllerTest extends AbstractTestNGSpringContextTests {


    @Autowired
    private KategoriProdukRepo kategoriProdukRepo;

    private JSONObject req;
    private KategoriProduk kategoriProduk;
    private Random rand ;
    private DataGenerator dataGenerator;
    private Optional<KategoriProduk> op;
    private Long sizeAllData = 0L;

    @BeforeClass
    private void init(){
        AuthControllerTest.token = new TokenGenerator(AuthControllerTest.token).getToken();
        rand  = new Random();
        req = new JSONObject();
        kategoriProduk = new KategoriProduk();
        dataGenerator = new DataGenerator();
        Optional<KategoriProduk> op = kategoriProdukRepo.findTop1ByOrderByIdDesc();
        kategoriProduk = op.get();
    }
    @Test(priority = 0)
    private void save(){
        Response response ;
        try{
            req.put("nama", dataGenerator.genDataAlfabet(10,40));

            response = given().
                    header("Content-Type","application/json").
                    header("accept","application/json").
                    header(AuthControllerTest.AUTH_HEADER,AuthControllerTest.token).
                    body(req).
                    request(Method.POST,"kategoriProduk");

            int intResponse = response.getStatusCode();
            JsonPath jsonPath = response.jsonPath();
            Assert.assertEquals(intResponse,201);
            Assert.assertEquals(jsonPath.getString("message"),"DATA BERHASIL DISIMPAN !!");
            Assert.assertNotNull(jsonPath.getString("data"));
            Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
            Assert.assertNotNull(jsonPath.getString("timestamp"));
        }catch (Exception e){
           Assert.assertNull("error "+e.getMessage());
        }
    }

    @Test(priority = 10)
    void update(){
        Response response ;
        req.clear();
        try{
            String reqNama = dataGenerator.genDataAlfabet(10,40);
            kategoriProduk.setNama(reqNama);
            req.put("nama", reqNama);

            response = given().
                    header("Content-Type","application/json").
                    header("accept","*/*").
                    header(AuthControllerTest.AUTH_HEADER,AuthControllerTest.token).
                    body(req).
                    request(Method.PUT,"/kategoriProduk/"+kategoriProduk.getId());

            int intResponse = response.getStatusCode();
            JsonPath jsonPath = response.jsonPath();
            Assert.assertEquals(intResponse,200);
            Assert.assertEquals(jsonPath.getString("message"),"DATA BERHASIL DIUBAH");
            Assert.assertNotNull(jsonPath.getString("data"));
            Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
            Assert.assertNotNull(jsonPath.getString("timestamp"));
        }catch (Exception e){
            Assert.assertNull("error "+e.getMessage());
        }
    }

    @Test(priority = 20)
    void findById(){
        Response response ;
        try{
            response = given().
                    header("Content-Type","application/json").
                    header("accept","application/json").
                    header(AuthControllerTest.AUTH_HEADER,AuthControllerTest.token).
                    request(Method.GET,"/kategoriProduk/"+kategoriProduk.getId());

            int intResponse = response.getStatusCode();
            JsonPath jsonPath = response.jsonPath();
            System.out.println(response.getBody().prettyPrint());
            Assert.assertEquals(intResponse,200);
            Assert.assertEquals(jsonPath.getString("message"),"DATA DITEMUKAN");
            Assert.assertEquals(Long.parseLong(jsonPath.getString("data.id")),kategoriProduk.getId());
            Assert.assertEquals(jsonPath.getString("data.nama"),kategoriProduk.getNama());
            Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
            Assert.assertNotNull(jsonPath.getString("timestamp"));
        }catch (Exception e){
            Assert.assertNull("error "+e.getMessage());
        }
    }

    @Test(priority = 30)
    void findAll(){
        Response response ;
        try{
            response = given().
                    header("Content-Type","application/json").
                    header("accept","application/json").
                    header(AuthControllerTest.AUTH_HEADER,AuthControllerTest.token).
                    request(Method.GET,"/kategoriProduk");

            int intResponse = response.getStatusCode();
            JsonPath jsonPath = response.jsonPath();
            List ltData = jsonPath.getList("data.content");//List<Map<String,Object>>
            sizeAllData = kategoriProdukRepo.count();
//            System.out.println(response.getBody().prettyPrint());
            Assert.assertEquals(intResponse,200);
            Assert.assertEquals(jsonPath.getString("message"),"DATA DITEMUKAN");
            Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
            Assert.assertNotNull(jsonPath.getString("timestamp"));
// ======================================================================================================================================================
            Assert.assertEquals(jsonPath.getString("data.sort_by"),"id");
            Assert.assertEquals(Integer.parseInt(jsonPath.getString("data.current_page")),0);
            Assert.assertEquals(jsonPath.getString("data.column_name"),"id");
            Assert.assertNotNull(jsonPath.getString("data.total_pages"));
            Assert.assertEquals(jsonPath.getString("data.sort"),"asc");
            Assert.assertEquals(Integer.parseInt(jsonPath.getString("data.size_per_page")), OtherConfig.getDefaultPaginationSize());
            Assert.assertEquals(jsonPath.getString("data.value"),"");
            Assert.assertEquals(Integer.parseInt(jsonPath.getString("data.total_data")),sizeAllData);
        }catch (Exception e){
            Assert.assertNull("error "+e.getMessage());
        }
    }

    @Test(priority = 40)
    void findByParam(){
        Response response ;
        String pathVariable = "/kategoriProduk/asc/id/0";
        String strValue = kategoriProduk.getNama();
//        oqvtGoEMrkepJyESWKvKfiOQlzlp
        strValue = strValue.substring(0,strValue.length()-2);
        Integer sizePerPage  = 3;
        try{
            response = given().
                    header("Content-Type","application/json").
                    header("accept","application/json").
                    header(AuthControllerTest.AUTH_HEADER,AuthControllerTest.token).
                    params("column","nama").
                    params("value",strValue).
                    params("size",sizePerPage).
                    request(Method.GET,pathVariable);
            int intResponse = response.getStatusCode();
            JsonPath jsonPath = response.jsonPath();
            List<Map<String,Object>> ltData = jsonPath.getList("data.content");
            int intData = ltData.size();
            Map<String,Object> map = ltData.get(0);

//            System.out.println(response.getBody().prettyPrint());
            Assert.assertEquals(intResponse,200);
            Assert.assertEquals(jsonPath.getString("message"),"DATA DITEMUKAN");
            Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
            Assert.assertNotNull(jsonPath.getString("timestamp"));
// ======================================================================================================================================================
            Assert.assertEquals(jsonPath.getString("data.sort_by"),"id");
            Assert.assertEquals(Integer.parseInt(jsonPath.getString("data.current_page")),0);
            Assert.assertEquals(jsonPath.getString("data.column_name"),"nama");
            Assert.assertNotNull(jsonPath.getString("data.total_pages"));
            Assert.assertEquals(Integer.parseInt(jsonPath.getString("data.total_data")),intData);
            Assert.assertEquals(jsonPath.getString("data.sort"),"asc");
            Assert.assertEquals(Integer.parseInt(jsonPath.getString("data.size_per_page")), sizePerPage);
            Assert.assertEquals(jsonPath.getString("data.value"),strValue);
// ======================================================================================================================================================
            Assert.assertEquals(map.get("nama"),kategoriProduk.getNama());
            Assert.assertEquals(Long.parseLong(map.get("id").toString()),kategoriProduk.getId());
        }catch (Exception e){
            Assert.assertNull("error "+e.getMessage());
        }
    }

    @Test(priority = 50)
    void uploadExcel(){
        Response response ;
        try{
            response = given().
                    header("Content-Type","multipart/form-data").
                    header("Accept","application/json").
                    header(AuthControllerTest.AUTH_HEADER,AuthControllerTest.token).
                    multiPart("filez",new File(System.getProperty("user.dir")+"/src/test/resources/data-test/kategori-produk.xlsx"),
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").
                    request(Method.POST,"kategoriProduk/upload-excel");

            int intResponse = response.getStatusCode();
            JsonPath jsonPath = response.jsonPath();
            Assert.assertEquals(intResponse,201);
            Assert.assertEquals(jsonPath.getString("message"),"UPLOAD FILE EXCEL BERHASIL");
            Assert.assertNotNull(jsonPath.getString("data"));
            Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
            Assert.assertNotNull(jsonPath.getString("timestamp"));
        }catch (Exception e){
            Assert.assertNull("error "+e.getMessage());
        }
    }
    @Test(priority = 60)
    void downloadExcel(){
        Response response ;
        try{
            response = given().
                    header("Content-Type","application/json").
                    header("accept","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").
                    header(AuthControllerTest.AUTH_HEADER,AuthControllerTest.token).
                    params("column","nama").
                    params("value",kategoriProduk.getNama()).
                    request(Method.GET,"kategoriProduk/download-excel");

            int intResponse = response.getStatusCode();
            Assert.assertEquals(intResponse,200);
            /** khusus untuk download file harus di cek header nya */
            Assert.assertTrue(response.getHeader("Content-Disposition").contains(".xlsx"));// file nya memiliki extension .xlsx
            Assert.assertEquals(response.getHeader("Content-Type"),"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");// content type wajib ini untuk excel
            Assert.assertNotNull(response.getBody());
        }catch (Exception e){
            Assert.assertNull("error "+e.getMessage());
        }
    }

    @Test(priority = 70)
    void downloadPdf(){
        Response response ;
        try{
            response = given().
                    header("Content-Type","application/json").
                    header("accept","application/pdf").
                    header(AuthControllerTest.AUTH_HEADER,AuthControllerTest.token).
                    params("column","nama").
                    params("value",kategoriProduk.getNama()).
                    request(Method.GET,"kategoriProduk/download-pdf");

            int intResponse = response.getStatusCode();
            Assert.assertEquals(intResponse,200);
            /** khusus untuk download file harus di cek header nya */
            Assert.assertTrue(response.getHeader("Content-Disposition").contains(".pdf"));// file nya memiliki extension .xlsx
            Assert.assertEquals(response.getHeader("Content-Type"),"application/pdf");// content type wajib ini untuk excel
            Assert.assertNotNull(response.getBody());
        }catch (Exception e){
            Assert.assertNull("error "+e.getMessage());
        }
    }

    @Test(priority = 999)
    void delete() {
        Response response;
        try {
            response = given().
                    header("Content-Type", "application/json").
                    header("Accept", "application/json").
                    header(AuthControllerTest.AUTH_HEADER, AuthControllerTest.token).
                    request(Method.DELETE, "/kategoriProduk/" + kategoriProduk.getId());

            int intResponse = response.getStatusCode();
            JsonPath jsonPath = response.jsonPath();
//            System.out.println(response.getBody().prettyPrint());
            Assert.assertEquals(intResponse, 200);
            Assert.assertEquals(jsonPath.getString("message"), "DATA BERHASIL DIHAPUS");
            Assert.assertNotNull(jsonPath.getString("data"));
            Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
            Assert.assertNotNull(jsonPath.getString("timestamp"));
        } catch (Exception e) {
            Assert.assertNull("error "+e.getMessage());
        }
    }
}