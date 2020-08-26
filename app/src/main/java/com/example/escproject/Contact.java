package com.example.escproject;

public class Contact {

    private String name;
    private String phone;
    private String email;

    public Contact(String name, String phone, String emil) {
        this.name = name;
        this.phone = phone;
        this.email = emil;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emil) {
        this.email = emil;
    }
}
