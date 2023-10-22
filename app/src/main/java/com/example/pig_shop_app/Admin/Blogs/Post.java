package com.example.pig_shop_app.Admin.Blogs;

public class Post {
    public String id;
    public String title;
    public String content;
    public String date;

    public String image;
    public Post() {
    }

    public Post(String id, String title, String content, String date,String image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.image =image;

    }

    // Getter và Setter cho các thuộc tính


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {

        return content;
    }
    public void setContent(String content) {

        this.content = content;
    }
    public String getDate() {

        return date;
    }
    public void setDate(String date) {

        this.date = date;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public  String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    }




