package com.example.fyp_project.Model;

public class Products {
    private String productID;
    private String name;
    private String image;
    private String description;
    private String price;
    private String time;
    private String date;
    private String category;

    public Products(){

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Products(String productID, String name, String image, String description, String price, String time, String date, String category) {
        this.productID = productID;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.time = time;
        this.date = date;
        this.category = category;
    }

//    public Products(String productID, String name, String image, String description, String price) {
//        this.productID = productID;
//        this.name = name;
//        this.image = image;
//        this.description = description;
//        this.price = price;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
