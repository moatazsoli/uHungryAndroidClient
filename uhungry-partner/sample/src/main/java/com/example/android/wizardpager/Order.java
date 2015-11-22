package com.example.android.wizardpager;

/**
 * Created by Moataz on 2015-11-22.
 */
import java.util.ArrayList;

public class Order {
    private String id, date, location,orderdetails,status,totalprice;

    public Order(String id, String date, String location, String orderdetails, String status, String totalprice) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.orderdetails = orderdetails;
        this.status = status;
        this.totalprice = totalprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(String orderdetails) {
        this.orderdetails = orderdetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }
}