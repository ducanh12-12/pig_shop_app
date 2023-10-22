package com.example.pig_shop_app.Admin.User;

public class AdminItems {
    private String adminId;
    private String name, date, phone, email, role, pass;

    //hàm khơỉ taọ gán giá trị măc dinh
    public AdminItems(String adminId, String name, String date, String phone, String email, String role, String pass) {
        this.adminId = adminId;
        this.name = name;
        this.date = date;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.pass = pass;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }


    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPass() {
        return pass;
    }
}
