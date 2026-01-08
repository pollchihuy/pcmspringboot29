package com.juaracoding.pcmspringboot29.contoh;

import com.juaracoding.pcmspringboot29.contoh.FungsiString;
import org.testng.Assert;
import org.testng.annotations.*;

//suite
public class TestFungsiStr {

    private FungsiString fungsiString;
    @BeforeClass
    public void init(){
        fungsiString=new FungsiString();
    }

    @Test
    public void lengthStr(){
        String cumi = "Cumi-Cumi";
        Integer lengthCumi = fungsiString.lengthStr(cumi);
        Assert.assertEquals(lengthCumi,9);
    }

    @AfterClass
    public void finish(){
        System.out.println("Finish Class TestFungsiStr");
    }

    @AfterSuite
    public void finishAll(){
        //taruh proses untuk mengakhiri testing keseluruhan class
        System.out.println("Finish Suite");
    }
}