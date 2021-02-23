package com.example.fyp_project.Model;

public class Cart {
    //private String orderID;
    private String productID;
    private String name;
    private String price;
    private String quantity;

    public Cart(){

    }

    public Cart(String productID, String name, String price, String quantity) {
        //this.orderID = orderID;
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

//    public String getOrderID() {
//        return orderID;
//    }
//
//    public void setOrderID(String orderID) {
//        this.orderID = orderID;
//    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
