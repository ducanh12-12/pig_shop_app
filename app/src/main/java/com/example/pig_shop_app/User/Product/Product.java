package com.example.pig_shop_app.User.Product;

public class Product {
    private String avatar,category_id,content,description,price,size,title,key;
    public Product() {
        // Empty constructor required for Firebase
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public  Product( String avatar, String description, String price, String size, String title) {
        this.avatar = avatar;
        this.description = description;
        this.price = price;
        this.size = size;
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
