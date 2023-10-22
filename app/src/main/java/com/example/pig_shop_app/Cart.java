package com.example.pig_shop_app;

public class Cart {
    private String product_id, key,fullName,phoneNumber,address;
    public Cart() {
        // Empty constructor required for Firebase
    }

    public String getProduct_id() {
        return product_id;
    }
    public String getKey() {
        return key;
    }


    public void setProduct_id(String cart_id) {
        this.product_id = product_id;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public  Cart(String product_id, String fullName, String phoneNumber, String address, String key) {
        this.key = key;
        this.product_id = product_id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
