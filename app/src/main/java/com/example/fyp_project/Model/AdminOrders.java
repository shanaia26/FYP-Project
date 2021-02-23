package com.example.fyp_project.Model;

public class AdminOrders {
    private String orderID;
    private String address;
    private String city;
    private String name;
    private String phone;
    private String status;
    private String totalAmount;

    public AdminOrders(){

    }

    public AdminOrders(String orderID, String address, String city, String name, String phone, String status, String totalAmount) {
        this.orderID = orderID;
        this.address = address;
        this.city = city;
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
