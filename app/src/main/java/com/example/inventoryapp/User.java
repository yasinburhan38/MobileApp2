package com.example.inventoryapp;

public class User {

    String naam , email;

    public User(){

    }
    public User(String naam , String email){
        this.naam= naam;
        this.email = email;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
