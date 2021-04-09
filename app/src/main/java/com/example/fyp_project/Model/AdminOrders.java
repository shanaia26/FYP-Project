package com.example.fyp_project.Model;

public class AdminOrders {
    private String orderID;
    private String address;
    private String city;
    private String name;
    private String phone;
    private String paymentStatus;
    private String shipmentStatus;
    private String totalAmount;

    public AdminOrders(){

    }

    public AdminOrders(String orderID, String address, String city, String name, String phone, String paymentStatus, String shipmentStatus, String totalAmount) {
        this.orderID = orderID;
        this.address = address;
        this.city = city;
        this.name = name;
        this.phone = phone;
        this.paymentStatus = paymentStatus;
        this.shipmentStatus = shipmentStatus;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
