package com.example.fyp_project.Model;

public class CustomerCart {
    private String orderID;
    private String productID;
    private String description;
    private String size;
    private String comment;
    private String shipmentStatus;

    public CustomerCart(){

    }

    public CustomerCart(String orderID, String productID, String description, String size, String comment, String shipmentStatus) {
        this.orderID = orderID;
        this.productID = productID;
        this.description = description;
        this.size = size;
        this.comment = comment;
        this.shipmentStatus = shipmentStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }
}
