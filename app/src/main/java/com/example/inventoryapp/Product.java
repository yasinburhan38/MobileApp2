package com.example.inventoryapp;

import java.io.Serializable;

public class Product implements Serializable {

    String naam, productcode, aantal, prijs;

    public Product(){

    }
    public Product(String naam, String productcode, String aantal, String prijs){
        this.naam = naam ;
        this.prijs = prijs;
        this.productcode = productcode;
        this.aantal=aantal;


    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getAantal() {
        return aantal;
    }

    public void setAantal(String aantal) {
        this.aantal = aantal;
    }

    public String getPrijs() {
        return prijs;
    }

    public void setPrijs(String prijs) {
        this.prijs = prijs;
    }
}
