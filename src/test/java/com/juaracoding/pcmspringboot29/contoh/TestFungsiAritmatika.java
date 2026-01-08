package com.juaracoding.pcmspringboot29.contoh;

import com.juaracoding.pcmspringboot29.contoh.FungsiArtimatika;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class TestFungsiAritmatika {

    private FungsiArtimatika fungsiArtimatika;
    double x = 8;
    double y = 12;

    @BeforeSuite
    public void initAll(){
        //taruh object / variable yang digunakan untuk testing keseluruhan class
    }

    @BeforeClass
    public void init(){
        fungsiArtimatika = new FungsiArtimatika();
    }

    @Test(priority = 0)
    public void tambah(){
        double hasil = fungsiArtimatika.tambah(x,y);
        Assert.assertEquals(hasil,20.0);
    }
    @Test(priority = 10)
    public void kurang(){
        double hasil = fungsiArtimatika.kurang(x,y);
        Assert.assertEquals(hasil,-4.0);
    }
    @Test(priority = 20)
    public void kali(){
        double hasil = fungsiArtimatika.kali(x,y);
        Assert.assertEquals(hasil,96.0);
    }
    @Test(priority = 30)
    public void bagi(){
        double hasil = fungsiArtimatika.bagi(x,y);
        Assert.assertEquals(hasil,0.6666666666666666);
    }

    @AfterClass
    public void finish(){
        System.out.println("Finish Class TestFungsiAritmatika");
    }
}
