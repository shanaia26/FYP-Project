package com.example.fyp_project.Model;

public class CustomerEnquiries {
    private String category;
    private String comment;
    private String date;
    private String description;
    private String enquiryStatus;
    private String image;
    private String name;
    private String price;
    private String productID;
    private String quantity;
    private String size;
    private String time;

    public CustomerEnquiries() {

    }

    public CustomerEnquiries(String category, String comment, String date, String description, String enquiryStatus, String image, String name, String price, String productID, String quantity, String size, String time) {
        this.category = category;
        this.comment = comment;
        this.date = date;
        this.description = description;
        this.enquiryStatus = enquiryStatus;
        this.image = image;
        this.name = name;
        this.price = price;
        this.productID = productID;
        this.quantity = quantity;
        this.size = size;
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnquiryStatus() {
        return enquiryStatus;
    }

    public void setEnquiryStatus(String enquiryStatus) {
        this.enquiryStatus = enquiryStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
