package com.juaracoding.pcmspringboot29.controller;

import com.juaracoding.pcmspringboot29.util.LoggingFile;
import com.juaracoding.pcmspringboot29.utils.CredentialsAuth;
import com.juaracoding.pcmspringboot29.utils.DataGenerator;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerTest extends AbstractTestNGSpringContextTests {

    private JSONObject req;
    private Random rand ;
    public static String token;
    private DataGenerator dataGenerator;

    private String username;// ini adalah variable penampung
    private String password;// ini adalah variable penampung
    private String email;// ini adalah variable penampung
    private String otp;// ini adalah variable penampung
    private Boolean isOk;// untuk menjaga step estafet
    /** karena gak ada standard yang pasti untuk header key token
     * maka key nya dibuat variable agar suatu saat jika berubah semisal menjadi JwtToken atuapun jwt_token
     * tinggal diubah disini saja value AUTH_HEADER nya.....
     */
    public static final String AUTH_HEADER = "Authorization";

    @BeforeClass
    void inits(){
        RestAssured.baseURI = "http://localhost:8080";
        rand = new Random();
        dataGenerator = new DataGenerator();
        req = new JSONObject();
    }

    @Test(priority = 0)
    void regis(){
        Response response;
        try {
            isOk=false;
            username = dataGenerator.dataUsername();
            password = dataGenerator.dataPassword();
            email = dataGenerator.dataEmail();

            req.put("username", username);
            req.put("password", password);
            req.put("email", email);
            req.put("alamat", dataGenerator.dataAlamat());
            req.put("no_hp", dataGenerator.dataNoHp());
            req.put("nama_lengkap",dataGenerator.dataNamaLengkap());
            req.put("tanggal_lahir",dataGenerator.dataTanggalLahir());

            response = given().
                    header("Content-Type","application/json").
                    header("accept","application/json").
                    body(req).
                    request(Method.POST,"auth/regis");
            int intResponse = response.getStatusCode();
            JsonPath jPath = response.jsonPath();
            otp = jPath.getString("data.otp");
            System.out.println("Isi Response Body : "+response.getBody().asPrettyString());
            if(otp!=null && intResponse==200 && email !=null){
                isOk=true;
            }
            Assert.assertEquals(intResponse,200);
            Assert.assertNotNull(otp);
            Assert.assertEquals(jPath.getString("data.email"),email);
            Assert.assertEquals(jPath.getString("message"),"OTP Terkirim, Cek Email !!");
            Assert.assertTrue(Boolean.parseBoolean(jPath.getString("success")));
            Assert.assertNotNull(jPath.getString("timestamp"));
        }catch (Exception e){
            LoggingFile.logException("TestAuthController","regis",e);
            Assert.assertNotNull(null);
        }
    }

    @Test(priority = 10)
    void verifyRegis(){
        Response response;
        req.clear();
        if(isOk){
            isOk=false;
            try {
                req.put("email", email);
                req.put("otp",otp);

                response = given().
                        header("Content-Type","application/json").
                        header("accept","application/json").
                        body(req).
                        request(Method.POST,"auth/verify-regis");
                int intResponse = response.getStatusCode();
                JsonPath jsonPath = response.jsonPath();
                System.out.println(response.getBody().asPrettyString());
                if(intResponse==200){
                    isOk=true;
                }
                Assert.assertEquals(intResponse,200);
                Assert.assertEquals(jsonPath.getString("message"),"Registrasi Berhasil !!");
                Assert.assertEquals(jsonPath.getString("data"),"");
                Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
                Assert.assertNotNull(jsonPath.getString("timestamp"));
            }catch (Exception e){
                isOk=false;
            }
        }else {
            Assert.assertNotNull(null);//untuk menyatakan bahwa ini error atau diskip
        }
    }
    @Test(priority = 20)
    void loginTest(){
        Response response;
        req.clear();
        if(isOk){
            try {
                req.put("username",username);
                req.put("password",password);

                response = given().
                        header("Content-Type","application/json").
                        header("accept","application/json").
                        body(req).
                        request(Method.POST,"auth/login");
                int intResponse = response.getStatusCode();
                JsonPath jsonPath = response.jsonPath();
                System.out.println(response.getBody().asPrettyString());
                if(intResponse==200){
                    isOk=true;
                }
                Assert.assertEquals(intResponse,200);
                Assert.assertEquals(jsonPath.getString("message"),"Login Berhasil !!");
//                Assert.assertNotNull(jsonPath.getString("data.menu"));//kebutuhan front end thymeleaf.
                Assert.assertNotNull(jsonPath.getString("data.token"));
                Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
                Assert.assertNotNull(jsonPath.getString("timestamp"));
            }catch (Exception e){
                isOk=false;
            }
        }else {
            Assert.assertNotNull(null);//untuk menyatakan bahwa ini error atau diskip
        }
    }

    @Test(priority = 30)
    void loginAdmin(){
        Response response;
        req.clear();
            try {
                req.put("username", CredentialsAuth.ADMIN_USER_NAME);
                req.put("password",CredentialsAuth.ADMIN_PASSWORD);
                response = given().
                        header("Content-Type","application/json").
                        header("accept","*/*").
                        body(req).
                        request(Method.POST,"auth/login");
                int intResponse = response.getStatusCode();
                JsonPath jsonPath = response.jsonPath();
                if(intResponse==200){
                    token = "Bearer "+jsonPath.getString("data.token");
                    isOk=true;
                }
                Assert.assertEquals(intResponse,200);
                Assert.assertEquals(jsonPath.getString("message"),"Login Berhasil !!");
                Assert.assertNotNull(jsonPath.getString("data.token"));
                Assert.assertTrue(Boolean.parseBoolean(jsonPath.getString("success")));
                Assert.assertNotNull(jsonPath.getString("timestamp"));
            }catch (Exception e){
                isOk=false;
            }
    }
}